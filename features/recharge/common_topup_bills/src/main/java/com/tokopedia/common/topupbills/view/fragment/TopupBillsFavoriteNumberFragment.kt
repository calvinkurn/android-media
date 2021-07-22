package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.UpdateFavoriteDetail
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.databinding.FragmentFavoriteNumberBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.utils.covertContactUriToContactData
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsFavoriteNumberListAdapter
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberMenuBottomSheet
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberModifyBottomSheet
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberErrorStateListener
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberMenuListener
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberModifyListener
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberShimmerDataView
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactoryImpl
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ERROR_FETCH_AFTER_DELETE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ERROR_FETCH_AFTER_UNDO_DELETE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ERROR_FETCH_AFTER_UPDATE
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TopupBillsFavoriteNumberFragment :
        BaseDaggerFragment(),
        OnFavoriteNumberClickListener,
        FavoriteNumberMenuListener,
        FavoriteNumberEmptyStateListener,
        FavoriteNumberModifyListener,
        FavoriteNumberErrorStateListener
{
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var commonTopupBillsAnalytics: CommonTopupBillsAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }

    private lateinit var numberListAdapter: TopupBillsFavoriteNumberListAdapter
    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: ArrayList<String>
    private lateinit var localCacheHandler: LocalCacheHandler
    protected lateinit var inputNumberActionType: InputNumberActionType

    private var currentCategoryName = ""
    private var number: String = ""
    private var isHideCoachmark = false
    private var lastDeletedNumber: UpdateFavoriteDetail? = null

    private var binding: FragmentFavoriteNumberBinding? = null
    private var operatorData: TelcoCatalogPrefixSelect? = null
    private var operatorList: HashMap<String, TelcoAttributesOperator> = hashMapOf()
    private var clientNumbers: List<TopupBillsSeamlessFavNumberItem> = listOf()

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return TopupBillsFavoriteNumberFragment::class.java.simpleName
    }

    @Suppress("UNCHECKED_CAST")
    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, "")
            number = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            dgCategoryIds = arguments.getStringArrayList(ARG_PARAM_DG_CATEGORY_IDS) ?: arrayListOf()
            operatorData = arguments.getParcelable(ARG_PARAM_CATALOG_PREFIX_SELECT)
            currentCategoryName = arguments.getString(ARG_PARAM_CATEGORY_NAME, "")
        }

        operatorData?.rechargeCatalogPrefixSelect?.let { saveTelcoOperator(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteNumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
        loadData()
        binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.requestFocus()
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
        localCacheHandler = LocalCacheHandler(context, CACHE_PREFERENCES_NAME)
        isHideCoachmark = getLocalCache(CACHE_SHOW_COACH_MARK_KEY)
    }

    fun initView() {
        setClientNumberInputType()
        if (number.isNotEmpty()) {
            binding?.run {
                commonTopupbillsSearchNumberInputView.searchBarTextField.setText(number)
                commonTopupbillsSearchNumberInputView.searchBarTextField.setSelection(number.length)
                commonTopupbillsSearchNumberInputView.searchBarIcon.clearAnimation()
                commonTopupbillsSearchNumberInputView.searchBarIcon.post {
                    commonTopupbillsSearchNumberInputView.searchBarIcon.animate().scaleX(1f).scaleY(1f).start()
                }
            }
        }

        binding?.commonTopupbillsSearchNumberInputView?.run {
            searchBarTextField.addTextChangedListener(getSearchTextWatcher)
            searchBarTextField.setOnEditorActionListener(getSearchNumberListener)
            searchBarTextField.onFocusChangeListener = getFocusChangeListener
            clearListener = { onSearchReset() }
            searchBarTextField.imeOptions = EditorInfo.IME_ACTION_DONE
        }

        binding?.commonTopupbillsSearchNumberContactPicker?.setOnClickListener {
            inputNumberActionType = InputNumberActionType.CONTACT
            navigateContact()
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val typeFactory = FavoriteNumberTypeFactoryImpl(this, this, this)
        numberListAdapter = TopupBillsFavoriteNumberListAdapter(
                getListOfShimmeringDataView(), typeFactory
        )

        binding?.commonTopupbillsFavoriteNumberRv?.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = numberListAdapter
        }
    }

    private fun observeData() {
        topUpBillsViewModel.seamlessFavNumberUpdateData.observe(viewLifecycleOwner, Observer {
            binding?.commonTopupbillsSearchNumberInputView?.clearFocus()
            when (it) {
                is Success -> onSuccessUpdateClientName()
                is Fail -> onFailedUpdateClientName()
            }
        })

        topUpBillsViewModel.seamlessFavNumberData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetFavoriteNumber(it.data.favoriteNumbers)
                is Fail -> onFailedGetFavoriteNumber(it.throwable)
            }
        })

        topUpBillsViewModel.seamlessFavNumberDeleteData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessDeleteClientName(it.data)
                is Fail -> onFailedDeleteClientName()
            }
        })
        
        topUpBillsViewModel.seamlessFavNumberUndoDeleteData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessUndoDeleteFavoriteNumber(it.data)
                is Fail -> onFailedUndoDeleteFavoriteNumber()
            }
        })
    }

    private fun loadData() {
        getSeamlessFavoriteNumber()
    }

    private fun onSuccessUndoDeleteFavoriteNumber(favoriteDetail: UpdateFavoriteDetail) {
        view?.let {
            Toaster.build(
                it, getString(R.string.common_topup_fav_number_success_undo_delete, favoriteDetail.clientNumber),
                Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
        getSeamlessFavoriteNumber()
    }

    private fun onFailedUndoDeleteFavoriteNumber() {
        numberListAdapter.setNumbers(
            CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(clientNumbers))
        val throwable = MessageErrorException(getString(R.string.common_topup_fav_number_failed_undo_delete))
        showErrorToaster(throwable, Toaster.LENGTH_SHORT,
            getString(R.string.common_topup_fav_number_refresh)) { getSeamlessFavoriteNumber() }
    }

    private fun onSuccessGetFavoriteNumber(newClientNumbers: List<TopupBillsSeamlessFavNumberItem>) {
        clientNumbers = newClientNumbers
        if (clientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(
                    CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(clientNumbers))
        } else {
            numberListAdapter.setNotFound(listOf(TopupBillsFavNumberNotFoundDataView()))
            commonTopupBillsAnalytics.eventImpressionFavoriteNumberEmptyState(
                    currentCategoryName, userSession.userId)
        }
        binding?.commonTopupbillsFavoriteNumberClue?.run {
            if (numberListAdapter.visitables.isNotEmpty() &&
                numberListAdapter.visitables[0] is TopupBillsFavNumberDataView) {
                show()
            } else {
                hide()
            }
        }

        if (!isHideCoachmark && numberListAdapter.visitables.isNotEmpty()) {
            if (numberListAdapter.visitables[0] is TopupBillsFavNumberDataView) {
                showCoachmark()
            }
        }
    }

    private fun onFailedGetFavoriteNumber(err: Throwable) {
        when (err.message) {
            ERROR_FETCH_AFTER_UPDATE -> {
                val throwable = MessageErrorException(
                    getString(R.string.common_topup_fav_number_failed_fetch_after_update))
                showErrorToaster(throwable, Toaster.LENGTH_SHORT,
                    getString(R.string.common_topup_fav_number_refresh)) { getSeamlessFavoriteNumber() } }
            ERROR_FETCH_AFTER_DELETE -> {
                val throwable = MessageErrorException(
                    getString(R.string.common_topup_fav_number_failed_fetch_after_delete))
                showErrorToaster(throwable, Toaster.LENGTH_SHORT,
                    getString(R.string.common_topup_fav_number_refresh)) { getSeamlessFavoriteNumber() } }
            ERROR_FETCH_AFTER_UNDO_DELETE -> {
                val throwable = MessageErrorException(
                    getString(R.string.common_topup_fav_number_failed_fetch_after_undo_delete))
                showErrorToaster(throwable, Toaster.LENGTH_SHORT,
                    getString(R.string.common_topup_fav_number_retry)) { undoDelete() } }
            else -> {
                numberListAdapter.setErrorState(listOf(TopupBillsFavNumberErrorDataView()))
            }
        }
    }

    private fun showErrorToaster(
        throwable: Throwable,
        length: Int,
        actionText: String? = null,
        clickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        view?.let {
            if (actionText.isNullOrEmpty()) {
                Toaster.build(it, ErrorHandler.getErrorMessage(requireContext(), throwable),
                    length, Toaster.TYPE_ERROR).show()
            } else {
                Toaster.build(
                    it, ErrorHandler.getErrorMessage(requireContext(), throwable),
                    length, Toaster.TYPE_ERROR, actionText, clickListener
                ).show()
            }
        }
    }

    private val getFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) inputNumberActionType = InputNumberActionType.MANUAL
    }

    private val getSearchNumberListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchSubmitted(textView.text.toString())
                return true
            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchDone(textView.text.toString())
                return true
            }
            return false
        }
    }

    private val getSearchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            text?.let { filterData(text.toString()) }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    private fun setClientNumberInputType() {
        binding?.commonTopupbillsSearchNumberInputView
                ?.searchBarTextField?.inputType = when (clientNumberType.toLowerCase()) {
            ClientNumberType.TYPE_INPUT_TEL -> InputType.TYPE_CLASS_PHONE
            ClientNumberType.TYPE_INPUT_NUMERIC -> InputType.TYPE_CLASS_NUMBER
            ClientNumberType.TYPE_INPUT_ALPHANUMERIC -> InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TopupBillsSeamlessFavNumberItem>()

        searchClientNumbers.addAll(clientNumbers.filter {
            it.clientNumber.contains(query)
        })

        if (searchClientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(
                    CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(searchClientNumbers)
            )
            binding?.commonTopupbillsFavoriteNumberClue?.show()
        } else {
            if (topUpBillsViewModel.seamlessFavNumberData.value is Success) {
                if (clientNumbers.isNotEmpty()) {
                    numberListAdapter.setEmptyState(listOf(TopupBillsFavNumberEmptyDataView()))
                } else {
                    numberListAdapter.setNotFound(listOf(TopupBillsFavNumberNotFoundDataView()))
                }
            } else {
                numberListAdapter.setErrorState(listOf(TopupBillsFavNumberErrorDataView()))
            }
            binding?.commonTopupbillsFavoriteNumberClue?.hide()
        }
    }

    fun onSearchSubmitted(text: String?) {
        //do nothing
    }

    fun onSearchDone(text: String) {
        navigateToPDP(InputNumberActionType.MANUAL)
    }

    fun onSearchReset() {
        binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.setText("")
        if (clientNumbers.isEmpty())
            numberListAdapter.setNotFound(listOf(TopupBillsFavNumberNotFoundDataView()))
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onFavoriteNumberClick(clientNumber: TopupBillsSeamlessFavNumberItem) {
        navigateToPDP(InputNumberActionType.FAVORITE, clientNumber)
    }

    override fun onContinueClicked() {
        val clientNumber = binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.text.toString()
        commonTopupBillsAnalytics.eventClickFavoriteNumberContinue(
                currentCategoryName, getOperatorNameByPrefix(clientNumber), userSession.userId
        )
        navigateToPDP(InputNumberActionType.MANUAL)
    }

    private fun navigateToPDP(
            inputNumberActionType: InputNumberActionType,
            clientNumber: TopupBillsSeamlessFavNumberItem? = null
    ) {
        activity?.run {
            val intent = Intent()
            val searchedClientNumber: TopupBillsSeamlessFavNumberItem = clientNumber
                    ?: TopupBillsSeamlessFavNumberItem(
                            clientNumber = binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.text.toString()
                    )

            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, searchedClientNumber)
            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, inputNumberActionType)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun navigateContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermission(this,
                    PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            context?.let { permissionCheckerHelper.onPermissionDenied(it, permissionText) }
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            context?.let { permissionCheckerHelper.onNeverAskAgain(it, permissionText) }
                        }

                        override fun onPermissionGranted() {
                            openContactPicker()
                        }
                    })
        } else {
            openContactPicker()
        }
    }

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            view?.let {
                Toaster.build(it, getString(R.string.common_topup_contact_not_found),
                    Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    private fun onSuccessUpdateClientName() {
        view?.let {
            Toaster.build(it, getString(R.string.common_topup_fav_number_success_update_name),
                Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
        getSeamlessFavoriteNumber()
    }

    private fun onFailedUpdateClientName() {
        numberListAdapter.setNumbers(
            CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(clientNumbers))
        val throwable = MessageErrorException(
            getString(R.string.common_topup_fav_number_failed_update_name))

        showErrorToaster(throwable, Toaster.LENGTH_SHORT)
    }

    private fun onSuccessDeleteClientName(deletedFavoriteNumber: UpdateFavoriteDetail) {
        view?.let {
            Toaster.build(it, getString(R.string.common_topup_fav_number_success_delete_name),
                Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL,
                getString(R.string.common_topup_fav_number_toaster_undo_delete)
            ) { undoDelete(deletedFavoriteNumber) }.show()
        }
        getSeamlessFavoriteNumber()

        val operatorName = getOperatorNameById(deletedFavoriteNumber.operatorID)
        commonTopupBillsAnalytics.eventImpressionFavoriteNumberSuccessDeleteToaster(
                currentCategoryName, operatorName, userSession.userId)
    }

    private fun onFailedDeleteClientName() {
        numberListAdapter.setNumbers(
            CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(clientNumbers))
        val throwable = MessageErrorException(getString(R.string.common_topup_fav_number_failed_delete_name))
        showErrorToaster(throwable, Toaster.LENGTH_SHORT)
    }

    private fun undoDelete(deletedFavoriteNumber: UpdateFavoriteDetail? = null) {
        val favoriteDetail = deletedFavoriteNumber ?: lastDeletedNumber
        val isDelete = false
        showShimmering()
        if (favoriteDetail != null) {
            topUpBillsViewModel.modifySeamlessFavoriteNumber(
                CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberUpdateParams(
                    categoryId = favoriteDetail.categoryID,
                    productId = favoriteDetail.productID,
                    clientNumber = favoriteDetail.clientNumber,
                    totalTransaction = favoriteDetail.totalTransaction,
                    label = favoriteDetail.label,
                    isDelete = isDelete
                ),
                FavoriteNumberActionType.UNDO_DELETE
            )
        }
    }

    private fun getSeamlessFavoriteNumber() {
        showShimmering()
        topUpBillsViewModel.getSeamlessFavoriteNumbers(
                CommonTopupBillsGqlQuery.rechargeFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberParams(dgCategoryIds)
        )
    }

    private fun showShimmering() {
        numberListAdapter.setShimmer(getListOfShimmeringDataView())
    }

    private fun getListOfShimmeringDataView(): List<TopupBillsFavNumberShimmerDataView> {
        return listOf(
                TopupBillsFavNumberShimmerDataView(),
                TopupBillsFavNumberShimmerDataView(),
                TopupBillsFavNumberShimmerDataView(),
                TopupBillsFavNumberShimmerDataView()
        )
    }

    private fun showCoachmark() {
        Handler().run {
            postDelayed({
                context?.let {
                    val coachMarkItem = ArrayList<CoachMark2Item>()
                    val coachMark = CoachMark2(it)
                    val anchorView = getKebabMenuView()
                    anchorView?.let { anchor ->
                        coachMarkItem.add(0,
                                CoachMark2Item(
                                        anchor,
                                        getString(R.string.common_topup_fav_number_coachmark_1_title),
                                        getString(R.string.common_topup_fav_number_coachmark_1_subtitle),
                                        CoachMark2.POSITION_BOTTOM
                                )
                        )
                        coachMarkItem.add(1,
                                CoachMark2Item(
                                        anchor,
                                        getString(R.string.common_topup_fav_number_coachmark_2_title),
                                        getString(R.string.common_topup_fav_number_coachmark_2_subtitle),
                                        CoachMark2.POSITION_BOTTOM
                                )
                        )
                    }
                    coachMark.showCoachMark(coachMarkItem)
                }
                isHideCoachmark = true
                localCacheHandler.apply {
                    putBoolean(CACHE_SHOW_COACH_MARK_KEY, true)
                    applyEditor()
                }
            }, COACH_MARK_START_DELAY)
        }
        commonTopupBillsAnalytics.eventImpressionFavoriteNumberCoachmark(
                currentCategoryName, userSession.userId
        )
    }

    private fun getKebabMenuView(): View? {
        return binding?.commonTopupbillsFavoriteNumberRv?.findViewHolderForAdapterPosition(0)?.itemView
                ?.findViewById<IconUnify>(R.id.common_topupbills_favorite_number_menu)
    }

    private fun getLocalCache(key: String): Boolean {
        return localCacheHandler.getBoolean(key, false)
    }

    override fun onFavoriteNumberMenuClick(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId)
        commonTopupBillsAnalytics.eventClickFavoriteNumberKebabMenu(
                currentCategoryName, operatorName, userSession.userId)

        val shouldShowDelete = clientNumbers.size > 1

        val bottomSheet = FavoriteNumberMenuBottomSheet.newInstance(
                favNumberItem, this, shouldShowDelete)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onChangeNameMenuClicked(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId)
        commonTopupBillsAnalytics.eventImpressionEditBottomSheet(
                currentCategoryName, operatorName, userSession.userId)

        val bottomSheet = FavoriteNumberModifyBottomSheet.newInstance(favNumberItem, this)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onDeleteContactClicked(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        showDeleteConfirmationDialog(favNumberItem)
    }

    override fun onChangeName(newName: String, favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId)
        commonTopupBillsAnalytics.eventClickFavoriteNumberSaveBottomSheet(
                currentCategoryName, operatorName, userSession.userId)

        val isDelete = false
        showShimmering()
        topUpBillsViewModel.modifySeamlessFavoriteNumber(
                CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberUpdateParams(
                        categoryId = favNumberItem.categoryId,
                        productId = favNumberItem.productId,
                        clientNumber = favNumberItem.clientNumber,
                        totalTransaction = DEFAULT_TOTAL_TRANSACTION,
                        label = newName,
                        isDelete = isDelete
                ),
                FavoriteNumberActionType.UPDATE
        )
    }

    private fun showDeleteConfirmationDialog(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val clientDetail = if (favNumberItem.clientName.isNotEmpty()) {
            Html.fromHtml(
                getString(R.string.common_topup_fav_number_delete_dialog_with_client_name,
                    favNumberItem.clientName,
                    favNumberItem.clientNumber))
        } else {
            Html.fromHtml(
                getString(R.string.common_topup_fav_number_delete_dialog,
                    favNumberItem.clientNumber))
        }
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.run {
                setTitle(getString(R.string.common_topup_fav_number_delete_dialog_title))
                setDescription(clientDetail)
                setSecondaryCTAText(getString(R.string.common_topup_fav_number_delete_dialog_cancel))
                setSecondaryCTAClickListener { dismiss() }
                setPrimaryCTAText(getString(R.string.common_topup_fav_number_delete_dialog_confirm))
                setPrimaryCTAClickListener {
                    onConfirmDelete(favNumberItem)
                    dismiss()
                }
                show()
            }
        }

        val operatorName = getOperatorNameById(favNumberItem.operatorId)
        commonTopupBillsAnalytics.eventImpressionFavoriteNumberDeletePopUp(
                currentCategoryName, operatorName, userSession.userId
        )
    }

    private fun onConfirmDelete(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId)
        commonTopupBillsAnalytics.eventClickFavoriteNumberConfirmDelete(
                currentCategoryName, operatorName, userSession.userId
        )

        val isDelete = true
        showShimmering()
        topUpBillsViewModel.modifySeamlessFavoriteNumber(
                CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberUpdateParams(
                        categoryId = favNumberItem.categoryId,
                        productId = favNumberItem.productId,
                        clientNumber = favNumberItem.clientNumber,
                        totalTransaction = DEFAULT_TOTAL_TRANSACTION,
                        label = favNumberItem.clientName,
                        isDelete = isDelete
                ),
                FavoriteNumberActionType.DELETE
        ) {
            commonTopupBillsAnalytics.eventImpressionFavoriteNumberFailedDeleteToaster(
                    currentCategoryName, operatorName, userSession.userId
            )
        }
    }

    override fun refreshFavoriteNumberPage() {
        getSeamlessFavoriteNumber()
    }

    enum class InputNumberActionType {
        MANUAL, CONTACT, FAVORITE
    }

    enum class FavoriteNumberActionType {
        UPDATE, DELETE, UNDO_DELETE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        data.data?.run {
                            val contact = this.covertContactUriToContactData(it.contentResolver)
                            val clientNumber = TopupBillsSeamlessFavNumberItem(
                                    clientName = contact.givenName,
                                    clientNumber = contact.contactNumber)
                            navigateToPDP(InputNumberActionType.CONTACT, clientNumber)
                        }
                    }
                }
            }
        }
    }

    private fun saveTelcoOperator(rechargeCatalogPrefixSelect: RechargeCatalogPrefixSelect) {
        val operatorList = HashMap<String, TelcoAttributesOperator>()

        rechargeCatalogPrefixSelect.prefixes.forEach {
            if (!operatorList.containsKey(it.operator.id)) {
                operatorList[it.operator.id] = it.operator.attributes
            }
        }

        this.operatorList = operatorList
    }

    private fun getOperatorNameById(operatorId: Int): String {
        return operatorList[operatorId.toString()]?.name ?: ""
    }

    private fun getOperatorNameByPrefix(clientNumber: String): String {
        return this.operatorData?.rechargeCatalogPrefixSelect?.prefixes?.singleOrNull() {
            clientNumber.startsWith(it.value)
        }?.operator?.attributes?.name ?: ""
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(it,
                    requestCode, permissions,
                    grantResults)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_CONTACT_PICKER = 75

        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE = "ARG_PARAM_EXTRA_CLIENT_NUMBER"
        const val ARG_PARAM_CATALOG_PREFIX_SELECT = "ARG_PARAM_CATALOG_PREFIX_SELECT"
        const val ARG_PARAM_DG_CATEGORY_IDS = "ARG_PARAM_DG_CATEGORY_IDS"
        const val ARG_PARAM_CATEGORY_NAME = "ARG_PARAM_CATEGORY_NAME"
        const val COACH_MARK_START_DELAY: Long = 200
        const val CACHE_SHOW_COACH_MARK_KEY = "show_coach_mark_key_favorite_number"
        const val CACHE_PREFERENCES_NAME = "favorite_number_preferences"

        private const val DEFAULT_TOTAL_TRANSACTION = 0

        fun newInstance(clientNumberType: String, number: String,
                        operatorData: TelcoCatalogPrefixSelect?,
                        categoryName: String, digitalCategoryIds: ArrayList<String>
        ): Fragment {
            val fragment = TopupBillsFavoriteNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, number)
            bundle.putString(ARG_PARAM_CATEGORY_NAME, categoryName.toLowerCase(Locale.getDefault()))
            bundle.putStringArrayList(ARG_PARAM_DG_CATEGORY_IDS, digitalCategoryIds)
            bundle.putParcelable(ARG_PARAM_CATALOG_PREFIX_SELECT, operatorData)
            fragment.arguments = bundle
            return fragment
        }
    }
}
