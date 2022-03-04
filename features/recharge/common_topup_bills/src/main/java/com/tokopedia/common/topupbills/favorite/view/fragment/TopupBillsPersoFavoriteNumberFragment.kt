package com.tokopedia.common.topupbills.favorite.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.databinding.FragmentFavoriteNumberBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.favorite.data.UpdateFavoriteDetail
import com.tokopedia.common.topupbills.favorite.util.FavoriteNumberDataMapper
import com.tokopedia.common.topupbills.favorite.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favorite.view.adapter.TopupBillsPersoFavoriteNumberListAdapter
import com.tokopedia.common.topupbills.favorite.view.bottomsheet.PersoFavoriteNumberMenuBottomSheet
import com.tokopedia.common.topupbills.favorite.view.bottomsheet.PersoFavoriteNumberMenuBottomSheet.PersoFavoriteNumberMenuListener
import com.tokopedia.common.topupbills.favorite.view.bottomsheet.PersoFavoriteNumberModifyBottomSheet
import com.tokopedia.common.topupbills.favorite.view.bottomsheet.PersoFavoriteNumberModifyBottomSheet.PersoFavoriteNumberModifyListener
import com.tokopedia.common.topupbills.favorite.view.listener.FavoriteNumberDeletionListener
import com.tokopedia.common.topupbills.favorite.view.listener.PersoFavoriteNumberNotFoundStateListener
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberEmptyDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberErrorDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberShimmerDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.favorite.view.typefactory.PersoFavoriteNumberTypeFactoryImpl
import com.tokopedia.common.topupbills.favorite.view.util.FavoriteNumberActionType
import com.tokopedia.common.topupbills.utils.InputNumberActionType
import com.tokopedia.common.topupbills.favorite.view.viewmodel.TopupBillsFavNumberViewModel
import com.tokopedia.common.topupbills.favorite.view.viewmodel.TopupBillsFavNumberViewModel.Companion.ERROR_FETCH_AFTER_DELETE
import com.tokopedia.common.topupbills.favorite.view.viewmodel.TopupBillsFavNumberViewModel.Companion.ERROR_FETCH_AFTER_UNDO_DELETE
import com.tokopedia.common.topupbills.favorite.view.viewmodel.TopupBillsFavNumberViewModel.Companion.ERROR_FETCH_AFTER_UPDATE
import com.tokopedia.common.topupbills.favorite.view.viewholder.PersoFavoriteNumberErrorViewHolder.PersoFavoriteNumberErrorStateListener
import com.tokopedia.common.topupbills.favorite.view.viewholder.PersoFavoriteNumberViewHolder.OnPersoFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
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
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by misael on 09/02/22
 * This is the new general favorite number page,
 * We use gql from perso (fetch favnum) and retention (update, delete, undodelete) named "seamless"
 * */

class TopupBillsPersoFavoriteNumberFragment :
    BaseDaggerFragment(),
    OnPersoFavoriteNumberClickListener,
    PersoFavoriteNumberMenuListener,
    PersoFavoriteNumberModifyListener,
    PersoFavoriteNumberNotFoundStateListener,
    PersoFavoriteNumberErrorStateListener {
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var commonTopupBillsAnalytics: CommonTopupBillsAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )
    }
    private val favNumberViewModel by lazy { viewModelFragmentProvider.get(
        TopupBillsFavNumberViewModel::class.java) }
    private val savedNumberViewModel by lazy { viewModelFragmentProvider.get(
        TopupBillsSavedNumberViewModel::class.java) }

    private lateinit var numberListAdapter: TopupBillsPersoFavoriteNumberListAdapter
    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: List<Int>
    private lateinit var dgOperatorIds: List<Int>
    private lateinit var localCacheHandler: LocalCacheHandler

    private var currentCategoryName = ""
    private var number: String = ""
    private var loyaltyStatus: String = ""
    private var isHideCoachmark = false
    private var lastDeletedNumber: UpdateFavoriteDetail? = null

    private var binding: FragmentFavoriteNumberBinding? = null
    private var operatorList: HashMap<String, TelcoAttributesOperator> = hashMapOf()
    private var clientNumbers: List<TopupBillsPersoFavNumberDataView> = listOf()

    private val getSearchTextWatcher = object : android.text.TextWatcher {
        override fun afterTextChanged(text: android.text.Editable?) {
            text?.let {
                filterData(text.toString())
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return TopupBillsPersoFavoriteNumberFragment::class.java.simpleName
    }

    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, "")
            number = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            dgCategoryIds =
                arguments.getIntegerArrayList(ARG_PARAM_DG_CATEGORY_IDS)?.toList() ?: listOf()
            dgOperatorIds =
                arguments.getIntegerArrayList(ARG_PARAM_DG_OPERATOR_IDS)?.toList() ?: listOf()
            currentCategoryName = arguments.getString(ARG_PARAM_CATEGORY_NAME, "")
            loyaltyStatus = arguments.getString(ARG_PARAM_LOYALTY_STATUS, "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
        localCacheHandler = LocalCacheHandler(context, CACHE_PREFERENCES_NAME)
        isHideCoachmark = getLocalCache(CACHE_SHOW_COACH_MARK_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val typeFactory = PersoFavoriteNumberTypeFactoryImpl(this, this, this)
        numberListAdapter = TopupBillsPersoFavoriteNumberListAdapter(
            getListOfShimmeringDataView(), typeFactory
        )

        binding?.commonTopupbillsFavoriteNumberRv?.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = numberListAdapter
        }
    }

    private fun observeData() {
        favNumberViewModel.seamlessFavNumberUpdateData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessUpdateClientName()
                is Fail -> onFailedUpdateClientName()
            }
        })

        favNumberViewModel.persoFavNumberData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetFavoriteNumber(it.data.first.items)
                is Fail -> onFailedGetFavoriteNumber(it.throwable)
            }
        })

        favNumberViewModel.seamlessFavNumberDeleteData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessDeleteClientName(it.data)
                is Fail -> onFailedDeleteClientName()
            }
        })

        favNumberViewModel.seamlessFavNumberUndoDeleteData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessUndoDeleteFavoriteNumber(it.data)
                is Fail -> onFailedUndoDeleteFavoriteNumber()
            }
        })

        savedNumberViewModel.searchKeyword.observe(viewLifecycleOwner, { keyword ->
            filterData(keyword) })

        savedNumberViewModel.refreshSearchBar.observe(viewLifecycleOwner, { position ->
            if (position == DualTabSavedNumberFragment.POSITION_FAVORITE_NUMBER) {
                savedNumberViewModel.setClueVisibility(clientNumbers.isNotEmpty())
                savedNumberViewModel.enableSearchBar(clientNumbers.isNotEmpty())
            }
        })
    }

    private fun loadData() {
        getPersoFavoriteNumber()
    }

    private fun getPersoFavoriteNumber() {
        showShimmering()
        favNumberViewModel.getPersoFavoriteNumbers(dgCategoryIds, dgOperatorIds)
    }

    private fun onSuccessUndoDeleteFavoriteNumber(favoriteDetail: UpdateFavoriteDetail) {
        view?.let {
            Toaster.build(
                it,
                getString(
                    R.string.common_topup_fav_number_success_undo_delete,
                    favoriteDetail.clientNumber
                ),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL
            ).show()
        }
        getPersoFavoriteNumber()
    }

    private fun onFailedUndoDeleteFavoriteNumber() {
        numberListAdapter.setNumbers(clientNumbers)
        val throwable =
            MessageErrorException(getString(R.string.common_topup_fav_number_failed_undo_delete))
        showErrorToaster(
            throwable, Toaster.LENGTH_SHORT,
            getString(R.string.common_topup_fav_number_retry)
        ) { getPersoFavoriteNumber() }
    }

    private fun onSuccessGetFavoriteNumber(newClientNumbers: List<TopupBillsPersoFavNumberItem>) {
        clientNumbers = FavoriteNumberDataMapper.mapPersoFavNumberItemToDataView(newClientNumbers)
        val namedFavNumber = clientNumbers.count { it.subtitle.isNotEmpty() }
        commonTopupBillsAnalytics.eventImpressionTotalFavoriteNumber(
            totalUnnamedFavNumber = clientNumbers.size - namedFavNumber,
            totalNamedFavNumber = namedFavNumber,
            userId = userSession.userId
        )

        if (clientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(clientNumbers)
        } else {
            numberListAdapter.setNotFound(listOf(TopupBillsPersoFavNumberNotFoundDataView()))
            commonTopupBillsAnalytics.eventImpressionFavoriteNumberEmptyState(
                currentCategoryName, userSession.userId
            )
        }

        if (numberListAdapter.visitables.isNotEmpty() &&
            numberListAdapter.visitables[0] is TopupBillsPersoFavNumberDataView
        ) {
            binding?.run {
                savedNumberViewModel.setClueVisibility(true)
                savedNumberViewModel.enableSearchBar(true)
            }
        } else {
            binding?.run {
                savedNumberViewModel.setClueVisibility(false)
                savedNumberViewModel.enableSearchBar(false)
            }
        }

        if (!isHideCoachmark && numberListAdapter.visitables.isNotEmpty()) {
            if (numberListAdapter.visitables[0] is TopupBillsPersoFavNumberDataView) {
                showCoachmark()
            }
        }
    }

    private fun onFailedGetFavoriteNumber(err: Throwable) {
        when (err.message) {
            ERROR_FETCH_AFTER_UPDATE -> {
                val throwable = MessageErrorException(
                    getString(R.string.common_topup_fav_number_failed_fetch_after_update)
                )
                showErrorToaster(
                    throwable, Toaster.LENGTH_SHORT,
                    getString(R.string.common_topup_fav_number_refresh)
                ) { getPersoFavoriteNumber() }
            }
            ERROR_FETCH_AFTER_DELETE -> {
                val throwable = MessageErrorException(
                    getString(R.string.common_topup_fav_number_failed_fetch_after_delete)
                )
                showErrorToaster(
                    throwable, Toaster.LENGTH_SHORT,
                    getString(R.string.common_topup_fav_number_refresh)
                ) { getPersoFavoriteNumber() }
            }
            ERROR_FETCH_AFTER_UNDO_DELETE -> {
                val throwable = MessageErrorException(
                    getString(R.string.common_topup_fav_number_failed_fetch_after_undo_delete)
                )
                showErrorToaster(
                    throwable, Toaster.LENGTH_SHORT,
                    getString(R.string.common_topup_fav_number_retry)
                ) { undoDelete() }
            }
            else -> {
                numberListAdapter.setErrorState(listOf(TopupBillsPersoFavNumberErrorDataView()))
                savedNumberViewModel.setClueVisibility(false)
            }
        }
    }

    private fun onSuccessUpdateClientName() {
        view?.let {
            Toaster.build(
                it, getString(R.string.common_topup_fav_number_success_update_name),
                Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL
            ).show()
        }
        getPersoFavoriteNumber()
    }

    private fun onFailedUpdateClientName() {
        numberListAdapter.setNumbers(clientNumbers)
        val throwable = MessageErrorException(
            getString(R.string.common_topup_fav_number_failed_update_name)
        )

        showErrorToaster(throwable, Toaster.LENGTH_SHORT)
    }

    private fun onSuccessDeleteClientName(deletedFavoriteNumber: UpdateFavoriteDetail) {
        view?.let {
            Toaster.build(
                it,
                getString(
                    R.string.common_topup_fav_number_success_delete_name,
                    deletedFavoriteNumber.clientNumber
                ),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getString(R.string.common_topup_fav_number_toaster_undo_delete)
            ) { undoDelete(deletedFavoriteNumber) }.show()
        }
        getPersoFavoriteNumber()
    }

    private fun onFailedDeleteClientName() {
        numberListAdapter.setNumbers(clientNumbers)
        val throwable =
            MessageErrorException(getString(R.string.common_topup_fav_number_failed_delete_name))
        showErrorToaster(throwable, Toaster.LENGTH_SHORT)
    }

    private fun undoDelete(deletedFavoriteNumber: UpdateFavoriteDetail? = null) {
        val favoriteDetail = deletedFavoriteNumber ?: lastDeletedNumber
        val shouldDelete = false
        showShimmering()
        if (favoriteDetail != null) {
            favNumberViewModel.modifySeamlessFavoriteNumber(
                categoryId = favoriteDetail.categoryID,
                productId = favoriteDetail.productID,
                clientNumber = favoriteDetail.clientNumber,
                totalTransaction = favoriteDetail.totalTransaction,
                label = favoriteDetail.label,
                isDelete = shouldDelete,
                actionType = FavoriteNumberActionType.UNDO_DELETE
            )
        }
    }

    private fun showShimmering() {
        numberListAdapter.setShimmer(getListOfShimmeringDataView())
    }

    private fun getListOfShimmeringDataView(): List<TopupBillsPersoFavNumberShimmerDataView> {
        return listOf(
            TopupBillsPersoFavNumberShimmerDataView(),
            TopupBillsPersoFavNumberShimmerDataView(),
            TopupBillsPersoFavNumberShimmerDataView(),
            TopupBillsPersoFavNumberShimmerDataView()
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
                        coachMarkItem.add(
                            0,
                            CoachMark2Item(
                                anchor,
                                getString(R.string.common_topup_fav_number_coachmark_1_title),
                                getString(R.string.common_topup_fav_number_coachmark_1_subtitle),
                                CoachMark2.POSITION_BOTTOM
                            )
                        )
                        coachMarkItem.add(
                            1,
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
                Toaster.build(
                    it, errorMessage.orEmpty(), length, Toaster.TYPE_ERROR,
                    actionText, clickListener
                ).show()
            }
        }
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TopupBillsPersoFavNumberDataView>()

        searchClientNumbers.addAll(clientNumbers.filter {
            it.title.contains(query, true) || it.subtitle.contains(query, true)
        })

        if (searchClientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(searchClientNumbers)
            if (isVisible) {
                savedNumberViewModel.setClueVisibility(true)
            }
        } else {
            if (favNumberViewModel.persoFavNumberData.value is Success) {
                if (clientNumbers.isNotEmpty()) {
                    numberListAdapter.setEmptyState(listOf(TopupBillsPersoFavNumberEmptyDataView()))
                } else {
                    numberListAdapter.setNotFound(listOf(TopupBillsPersoFavNumberNotFoundDataView()))
                }
            }
            if (isVisible) {
                savedNumberViewModel.setClueVisibility(false)
            }
        }
    }

    override fun onFavoriteNumberClick(
        clientNumber: TopupBillsPersoFavNumberDataView,
        position: Int
    ) {
        val namedFavNumber = clientNumbers.count { it.subtitle.isNotEmpty() }
        commonTopupBillsAnalytics.eventClickTotalFavoriteNumber(
            totalUnnamedFavNumber = clientNumbers.size - namedFavNumber,
            totalNamedFavNumber = namedFavNumber,
            clickPosition = position,
            userId = userSession.userId
        )
        navigateToPDP(InputNumberActionType.FAVORITE, clientNumber)
    }

    override fun onFavoriteNumberMenuClick(favNumberItem: TopupBillsPersoFavNumberDataView) {
        commonTopupBillsAnalytics.eventClickFavoriteNumberKebabMenu(
            currentCategoryName, favNumberItem.operatorName, userSession.userId
        )

        val shouldShowDelete = clientNumbers.size > MIN_TOTAL_FAV_NUMBER

        val bottomSheet = PersoFavoriteNumberMenuBottomSheet.newInstance(
            favNumberItem, this, shouldShowDelete
        )
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onChangeName(newName: String, favNumberItem: TopupBillsPersoFavNumberDataView) {
        commonTopupBillsAnalytics.eventClickFavoriteNumberSaveBottomSheet(
            currentCategoryName, favNumberItem.operatorName, userSession.userId
        )

        val shouldDelete = false

        showShimmering()
        favNumberViewModel.modifySeamlessFavoriteNumber(
            categoryId = favNumberItem.categoryId.toIntOrZero(),
            productId = favNumberItem.productId.toIntOrZero(),
            clientNumber = favNumberItem.getClientNumber(),
            totalTransaction = DEFAULT_TOTAL_TRANSACTION,
            label = newName,
            isDelete = shouldDelete,
            actionType = FavoriteNumberActionType.UPDATE
        )
    }

    override fun onChangeNameMenuClicked(favNumberItem: TopupBillsPersoFavNumberDataView) {
        commonTopupBillsAnalytics.eventImpressionEditBottomSheet(
            currentCategoryName, favNumberItem.operatorName, userSession.userId
        )
        commonTopupBillsAnalytics.eventClickMenuFavoriteNumberModify(
            currentCategoryName, favNumberItem.operatorName, loyaltyStatus, userSession.userId
        )

        val bottomSheet = PersoFavoriteNumberModifyBottomSheet.newInstance(favNumberItem, this)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onDeleteContactClicked(favNumberItem: TopupBillsPersoFavNumberDataView) {
        commonTopupBillsAnalytics.eventClickMenuFavoriteNumberDelete(
            currentCategoryName, favNumberItem.operatorName, loyaltyStatus, userSession.userId
        )
        showDeleteConfirmationDialog(favNumberItem)
    }

    private fun showDeleteConfirmationDialog(favNumberItem: TopupBillsPersoFavNumberDataView) {
        val clientName = favNumberItem.getClientName()
        val clientNumber = favNumberItem.getClientNumber()

        val clientDetail = if (favNumberItem.getClientName().isNotEmpty()) {
            Html.fromHtml(
                getString(
                    R.string.common_topup_fav_number_delete_dialog_with_client_name,
                    clientName,
                    clientNumber
                )
            )
        } else {
            Html.fromHtml(
                getString(
                    R.string.common_topup_fav_number_delete_dialog,
                    clientNumber
                )
            )
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

        commonTopupBillsAnalytics.eventImpressionFavoriteNumberDeletePopUp(
            currentCategoryName, favNumberItem.operatorName, userSession.userId
        )
    }

    private fun onConfirmDelete(favNumberItem: TopupBillsPersoFavNumberDataView) {
        commonTopupBillsAnalytics.eventClickFavoriteNumberConfirmDelete(
            currentCategoryName, favNumberItem.operatorName, userSession.userId
        )

        val shouldDelete = true
        showShimmering()
        favNumberViewModel.modifySeamlessFavoriteNumber(
            categoryId = favNumberItem.categoryId.toIntOrZero(),
            productId = favNumberItem.productId.toIntOrZero(),
            clientNumber = favNumberItem.getClientNumber(),
            totalTransaction = DEFAULT_TOTAL_TRANSACTION,
            label = favNumberItem.getClientName(),
            isDelete = shouldDelete,
            actionType = FavoriteNumberActionType.DELETE,
            operatorName = favNumberItem.operatorName,
            object: FavoriteNumberDeletionListener {
                override fun onSuccessDelete(operatorName: String) {
                    commonTopupBillsAnalytics.eventImpressionFavoriteNumberSuccessDeleteToaster(
                        currentCategoryName, operatorName, userSession.userId
                    )
                }

                override fun onFailedDelete() {
                    commonTopupBillsAnalytics.eventImpressionFavoriteNumberFailedDeleteToaster(
                        currentCategoryName, favNumberItem.operatorName, userSession.userId
                    )
                }
            }
        )
    }

    override fun onContinueClicked() {
        commonTopupBillsAnalytics.eventClickFavoriteNumberContinue(
            currentCategoryName, userSession.userId
        )
        activity?.finish()
    }

    override fun onCloseClick(favNumberItem: TopupBillsPersoFavNumberDataView) {
        commonTopupBillsAnalytics.eventClickMenuCancelFavoriteNumberModify(
            currentCategoryName, favNumberItem.operatorName, loyaltyStatus, userSession.userId
        )
    }

    override fun refreshFavoriteNumberPage() {
        getPersoFavoriteNumber()
    }

    private fun navigateToPDP(
        inputNumberActionType: InputNumberActionType? = null,
        favNumber: TopupBillsPersoFavNumberDataView? = null
    ) {
        activity?.run {
            val intent = Intent()
            val searchedNumber = TopupBillsSavedNumber(
                clientName = favNumber?.getClientName() ?: "",
                clientNumber = favNumber?.getClientNumber() ?: "",
                categoryId = favNumber?.categoryId ?: "",
                productId = favNumber?.productId ?: "",
                inputNumberActionTypeIndex = inputNumberActionType?.ordinal ?: -1,
                operatorId = favNumber?.operatorId ?: ""
            )

            intent.putExtra(
                EXTRA_CALLBACK_CLIENT_NUMBER,
                searchedNumber
            )
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun getLocalCache(key: String): Boolean {
        return localCacheHandler.getBoolean(key, false)
    }

    private fun getOperatorNameById(operatorId: Int): String {
        return operatorList[operatorId.toString()]?.name ?: ""
    }

    companion object {
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE = "ARG_PARAM_EXTRA_CLIENT_NUMBER"
        const val ARG_PARAM_DG_CATEGORY_IDS = "ARG_PARAM_DG_CATEGORY_IDS"
        const val ARG_PARAM_DG_OPERATOR_IDS = "ARG_PARAM_DG_OPERATOR_IDS"
        const val ARG_PARAM_CATEGORY_NAME = "ARG_PARAM_CATEGORY_NAME"
        const val ARG_PARAM_LOYALTY_STATUS = "ARG_PARAM_LOYALTY_STATUS"
        const val COACH_MARK_START_DELAY: Long = 200
        const val CACHE_SHOW_COACH_MARK_KEY = "show_coach_mark_key_favorite_number"
        const val CACHE_PREFERENCES_NAME = "favorite_number_preferences"

        private const val DEFAULT_TOTAL_TRANSACTION = 0
        private const val MIN_TOTAL_FAV_NUMBER = 1

        fun newInstance(
            clientNumberType: String, number: String,
            categoryName: String, digitalCategoryIds: ArrayList<String>,
            digitalOperatorIds: ArrayList<String>, loyaltyStatus: String
        ): Fragment {
            val fragment = TopupBillsPersoFavoriteNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, number)
            bundle.putString(ARG_PARAM_CATEGORY_NAME, categoryName.lowercase())
            bundle.putString(ARG_PARAM_LOYALTY_STATUS, loyaltyStatus)
            bundle.putStringArrayList(ARG_PARAM_DG_CATEGORY_IDS, digitalCategoryIds)
            bundle.putStringArrayList(ARG_PARAM_DG_OPERATOR_IDS, digitalOperatorIds)
            fragment.arguments = bundle
            return fragment
        }
    }
}