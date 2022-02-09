package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
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
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberMenuBottomSheet
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberMenuBottomSheet.FavoriteNumberMenuListener
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberModifyBottomSheet
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberModifyBottomSheet.FavoriteNumberModifyListener
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.model.*
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberShimmerDataView
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactoryImpl
import com.tokopedia.common.topupbills.view.util.FavoriteNumberActionType
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberErrorViewHolder.FavoriteNumberErrorStateListener
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberViewHolder.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ERROR_FETCH_AFTER_DELETE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ERROR_FETCH_AFTER_UNDO_DELETE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ERROR_FETCH_AFTER_UPDATE
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TopupBillsFavoriteNumberFragment:
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
    private val savedNumberViewModel by lazy {
        viewModelFragmentProvider.get(TopupBillsSavedNumberViewModel::class.java) }

    private lateinit var numberListAdapter: TopupBillsFavoriteNumberListAdapter
    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: ArrayList<String>
    private lateinit var localCacheHandler: LocalCacheHandler
    protected lateinit var inputNumberActionType: TopupBillsSearchNumberFragment.InputNumberActionType

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

    override fun getScreenName(): String {
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
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
        localCacheHandler = LocalCacheHandler(context, CACHE_PREFERENCES_NAME)
        isHideCoachmark = getLocalCache(CACHE_SHOW_COACH_MARK_KEY)
    }

    fun initView() {
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
            when (it) {
                is Success -> onSuccessUpdateClientName()
                is Fail -> onFailedUpdateClientName()
            }
        })

        topUpBillsViewModel.seamlessFavNumberData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetFavoriteNumber(it.data.first.favoriteNumbers)
                is Fail -> onFailedGetFavoriteNumber(it.throwable)
            }
            savedNumberViewModel.searchKeyword.observe(viewLifecycleOwner, { filterData(it) })
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

        savedNumberViewModel.searchKeyword.observe(viewLifecycleOwner, { keyword ->
            filterData(keyword) })

        savedNumberViewModel.refreshSearchBar.observe(viewLifecycleOwner, { position ->
            if (position == TopupBillsSavedNumberFragment.POSITION_FAVORITE_NUMBER) {
                savedNumberViewModel.setClueVisibility(clientNumbers.isNotEmpty())
                savedNumberViewModel.enableSearchBar(clientNumbers.isNotEmpty())
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
            getString(R.string.common_topup_fav_number_retry)) { getSeamlessFavoriteNumber() }
    }

    private fun onSuccessGetFavoriteNumber(newClientNumbers: List<TopupBillsSeamlessFavNumberItem>) {
        clientNumbers = newClientNumbers
        val namedFavNumber = clientNumbers.count { it.clientName.isNotEmpty() }
        commonTopupBillsAnalytics.eventImpressionTotalFavoriteNumber(
            totalUnnamedFavNumber = clientNumbers.size - namedFavNumber,
            totalNamedFavNumber = namedFavNumber,
            userId = userSession.userId
        )

        if (clientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(
                    CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(clientNumbers))
        } else {
            numberListAdapter.setNotFound(listOf(TopupBillsFavNumberNotFoundDataView()))
            commonTopupBillsAnalytics.eventImpressionFavoriteNumberEmptyState(
                    currentCategoryName, userSession.userId)
        }

        if (numberListAdapter.visitables.isNotEmpty() &&
            numberListAdapter.visitables[0] is TopupBillsFavNumberDataView
        ) {
            savedNumberViewModel.setClueVisibility(true)
            savedNumberViewModel.enableSearchBar(true)
        } else {
            savedNumberViewModel.setClueVisibility(false)
            savedNumberViewModel.enableSearchBar(false)
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
                savedNumberViewModel.setClueVisibility(false)
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
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                throwable,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            if (actionText.isNullOrEmpty()) {
                Toaster.build(it, errorMessage.orEmpty(), length, Toaster.TYPE_ERROR).show()
            } else {
                Toaster.build(it, errorMessage.orEmpty(), length, Toaster.TYPE_ERROR,
                    actionText, clickListener).show()
            }
        }
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TopupBillsSeamlessFavNumberItem>()

        searchClientNumbers.addAll(clientNumbers.filter {
            it.clientName.contains(query, true) || it.clientNumber.contains(query, true)
        })

        if (searchClientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(
                    CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(searchClientNumbers)
            )
            if (isVisible) savedNumberViewModel.setClueVisibility(true)
        } else {
            if (topUpBillsViewModel.seamlessFavNumberData.value is Success) {
                if (clientNumbers.isNotEmpty()) {
                    numberListAdapter.setEmptyState(listOf(TopupBillsFavNumberEmptyDataView()))
                } else {
                    numberListAdapter.setNotFound(listOf(TopupBillsFavNumberNotFoundDataView()))
                }
            }
            if (isVisible) savedNumberViewModel.setClueVisibility(false)
        }
    }

    override fun onFavoriteNumberClick(clientNumber: TopupBillsSeamlessFavNumberItem, position: Int) {
        val namedFavNumber = clientNumbers.count { it.clientName.isNotEmpty() }
        commonTopupBillsAnalytics.eventClickTotalFavoriteNumber(
            totalUnnamedFavNumber = clientNumbers.size - namedFavNumber,
            totalNamedFavNumber = namedFavNumber,
            clickPosition = position,
            userId = userSession.userId
        )
        navigateToPDP(TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE, clientNumber)
    }

    override fun onContinueClicked() {
        commonTopupBillsAnalytics.eventClickFavoriteNumberContinue(
                currentCategoryName, userSession.userId)
        activity?.finish()
    }

    private fun navigateToPDP(
            inputNumberActionType: TopupBillsSearchNumberFragment.InputNumberActionType? = null,
            favNumber: TopupBillsSeamlessFavNumberItem? = null
    ) {
        activity?.run {
            val intent = Intent()
            val searchedNumber = TopupBillsSavedNumber(
                clientName = favNumber?.clientName ?: "",
                clientNumber = favNumber?.clientNumber ?: "",
                categoryId = favNumber?.categoryId ?: "",
                productId = favNumber?.productId ?: "",
                inputNumberActionTypeIndex = inputNumberActionType?.ordinal ?: -1
            )

            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, searchedNumber)
            setResult(Activity.RESULT_OK, intent)
            finish()
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
            Toaster.build(it, getString(R.string.common_topup_fav_number_success_delete_name, deletedFavoriteNumber.clientNumber),
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
        val shouldDelete = false
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
                    isDelete = shouldDelete
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
        val operatorName = getOperatorNameById(favNumberItem.operatorId.toIntOrZero())
        commonTopupBillsAnalytics.eventClickFavoriteNumberKebabMenu(
                currentCategoryName, operatorName, userSession.userId)

        val shouldShowDelete = clientNumbers.size > MIN_TOTAL_FAV_NUMBER

        val bottomSheet = FavoriteNumberMenuBottomSheet.newInstance(
                favNumberItem, this, shouldShowDelete)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onChangeNameMenuClicked(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId.toIntOrZero())
        commonTopupBillsAnalytics.eventImpressionEditBottomSheet(
                currentCategoryName, operatorName, userSession.userId)

        val bottomSheet = FavoriteNumberModifyBottomSheet.newInstance(favNumberItem, this)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onDeleteContactClicked(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        showDeleteConfirmationDialog(favNumberItem)
    }

    override fun onChangeName(newName: String, favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId.toIntOrZero())
        commonTopupBillsAnalytics.eventClickFavoriteNumberSaveBottomSheet(
                currentCategoryName, operatorName, userSession.userId)

        val shouldDelete = false
        showShimmering()
        topUpBillsViewModel.modifySeamlessFavoriteNumber(
                CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberUpdateParams(
                        categoryId = favNumberItem.categoryId.toIntOrZero(),
                        productId = favNumberItem.productId.toIntOrZero(),
                        clientNumber = favNumberItem.clientNumber,
                        totalTransaction = DEFAULT_TOTAL_TRANSACTION,
                        label = newName,
                        isDelete = shouldDelete
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

        val operatorName = getOperatorNameById(favNumberItem.operatorId.toIntOrZero())
        commonTopupBillsAnalytics.eventImpressionFavoriteNumberDeletePopUp(
                currentCategoryName, operatorName, userSession.userId
        )
    }

    private fun onConfirmDelete(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val operatorName = getOperatorNameById(favNumberItem.operatorId.toIntOrZero())
        commonTopupBillsAnalytics.eventClickFavoriteNumberConfirmDelete(
                currentCategoryName, operatorName, userSession.userId
        )

        val shouldDelete = true
        showShimmering()
        topUpBillsViewModel.modifySeamlessFavoriteNumber(
                CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberUpdateParams(
                        categoryId = favNumberItem.categoryId.toIntOrZero(),
                        productId = favNumberItem.productId.toIntOrZero(),
                        clientNumber = favNumberItem.clientNumber,
                        totalTransaction = DEFAULT_TOTAL_TRANSACTION,
                        label = favNumberItem.clientName,
                        isDelete = shouldDelete
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CONTACT_PICKER) {
                activity?.let {
                    data.data?.run {
                        val contact = this.covertContactUriToContactData(it.contentResolver)
                        val clientNumber = TopupBillsSeamlessFavNumberItem(
                                clientName = contact.givenName,
                                clientNumber = contact.contactNumber)
                        navigateToPDP(TopupBillsSearchNumberFragment.InputNumberActionType.CONTACT, clientNumber)
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
        private const val MIN_TOTAL_FAV_NUMBER = 1

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
