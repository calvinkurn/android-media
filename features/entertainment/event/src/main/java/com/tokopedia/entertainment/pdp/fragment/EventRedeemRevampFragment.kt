package com.tokopedia.entertainment.pdp.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.entertainment.databinding.FragmentEventRedeemRevampBinding
import com.tokopedia.entertainment.pdp.activity.EventRedeemActivity.Companion.EXTRA_URL_REDEEM
import com.tokopedia.entertainment.pdp.adapter.EventRedeemVisitorAdapter
import com.tokopedia.entertainment.pdp.bottomsheet.EventRedeemRevampBottomSheet
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.getAllRedeemedTime
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.getListParticipantDetails
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.isEmptyParticipant
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.isOneParticipant
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.getOneRedemptionPair
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.isStatusNotAllDisabled
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.participantToVisitableMapper
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Data
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Participant
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.ParticipantDetail
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventRedeemRevampViewModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import com.tokopedia.entertainment.R.string as redeemString
import com.tokopedia.unifyprinciples.R.color as redeemColor
import com.tokopedia.unifyprinciples.R.dimen as dimenUnify


/**
 * Author firmanda on 17,Nov,2022
 */
class EventRedeemRevampFragment : BaseDaggerFragment(),
    EventRedeemRevampBottomSheet.RedeemBottomSheetListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: EventRedeemRevampViewModel

    private var binding by autoClearedNullable<FragmentEventRedeemRevampBinding>()
    private var urlRedeem: String = ""

    override fun getScreenName(): String {
        return EventRedeemRevampFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlRedeem = arguments?.getString(EXTRA_URL_REDEEM, "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventRedeemRevampBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalRequest()
        renderUI()
        observeRedeemData()
        observeRedeem()
        observeOldRedeem()
    }

    private fun initalRequest() {
        hideMainLayout()
        hideRedeemLayout()
        hideGlobalError()
        if (!userSession.isLoggedIn) {
            showGlobalError(isNotLogin = true, isUrlEmpty = false, null)
        } else if (urlRedeem.isEmpty()) {
            showGlobalError(isNotLogin = false, isUrlEmpty = true, null)
        } else if (userSession.isLoggedIn && urlRedeem.isNotEmpty()) {
            requestRedeemData()
        }
    }

    private fun requestRedeemData() {
        showLoading()
        viewModel.setInputRedeemUrl(urlRedeem)
    }

    private fun redeemIds() {
        viewModel.setInputRedeemedUrl(urlRedeem)
    }

    private fun oldRedeem() {
        viewModel.setInputOldRedeemedUrl(urlRedeem)
    }

    private fun observeRedeemData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowRedeemData.collect {
                when (it) {
                    is Success -> {
                        showData(it.data.data)
                    }
                    is Fail -> {
                        showMainError(it.throwable)
                    }
                }
            }
        }
    }

    private fun observeRedeem() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowRedeem.collect {
                when (it) {
                    is Success -> {
                        showSuccessRedeem(isFromScan = false, viewModel.getCheckedIdsSize())
                    }
                    is Fail -> {
                        showErrorToaster(it.throwable.message)
                    }
                }
            }
        }
    }

    private fun observeOldRedeem() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowOldRedeem.collect {
                when (it) {
                    is Success -> {
                        showSuccessRedeem(isFromScan = false, viewModel.oldFlowQuantity)
                    }
                    is Fail -> {
                        showErrorToaster(it.throwable.message)
                    }
                }
            }
        }
    }

    private fun renderUI() {
        binding?.run {
            tfRedeem.icon1.setColorFilter(
                MethodChecker.getColor(
                    context, redeemColor.Unify_NN950
                ), PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun showMainError(throwable: Throwable) {
        hideLoading()
        hideMainLayout()
        hideRedeemLayout()
        showGlobalError(isNotLogin = false, isUrlEmpty = false, throwable)
    }

    /***
     * All Voucher Already Redeemed redeem.redemptionStatus > 0
    **/
    private fun showData(redeem: Data) {
        hideLoading()
        hideGlobalError()
        showMainLayout()
        renderMainLayout(redeem)

        if (redeem.redemptionStatus.isMoreThanZero()) {
            showSuccessRedeem(isFromScan = true, Int.ZERO, redeem.product.createdAt)
        } else {
            hideRedeemLayout()
        }
    }

    private fun showSuccessRedeem(isFromScan: Boolean, qty: Int, date: String = "") {
        hideLoading()
        hideGlobalError()
        showMainLayout()
        showRedeemLayout()
        renderSuccessRedeemLayout(isFromScan, qty, date)
    }

    private fun processRedeem() {
        if (viewModel.getCheckedIdsSize().isMoreThanZero()) {
            hideGlobalError()
            showLoading()
            redeemIds()
        } else {
            setErrorInput()
        }
    }

    private fun processOldRedeem() {
        hideGlobalError()
        showLoading()
        oldRedeem()
    }

    private fun getUpdateListRedemption(): List<Participant> {
        return viewModel.listRedemptions
    }

    private fun showLoading() {
        binding?.run {
            loaderRedeem.show()
        }
    }

    private fun hideLoading() {
        binding?.run {
            loaderRedeem.hide()
        }
    }

    private fun showGlobalError(isNotLogin: Boolean, isUrlEmpty: Boolean, throwable: Throwable?) {
        context?.let { context ->
            binding?.globalErrorRedeem?.run {
                errorSecondaryAction.hide()
                show()
                if (isNotLogin) {
                    errorTitle.text = context.resources.getString(redeemString.ent_redeem_not_login)
                    errorDescription.text =
                        context.resources.getString(redeemString.ent_redeem_revamp_not_login)
                    errorAction.hide()
                } else if (isUrlEmpty) {
                    errorTitle.text = context.resources.getString(redeemString.ent_redeem_url_null)
                    errorDescription.text =
                        context.resources.getString(redeemString.ent_redeem_revamp_empty_url)
                    errorAction.hide()
                } else {
                    errorTitle.text = ErrorHandler.getErrorMessage(context, throwable)
                    errorAction.setOnClickListener {
                        initalRequest()
                    }
                }
            }
        }
    }

    private fun hideGlobalError() {
        binding?.run {
            globalErrorRedeem.hide()
        }
    }

    private fun showMainLayout() {
        binding?.run {
            tgTitleBooker.show()
            tgTitleBookerName.show()
            tgValueBookerName.show()
            tgTitleDetailProduct.show()
            tgTitleProduct.show()
            tgValueProduct.show()
            tgTitleTypeTicket.show()
            tgValueTypeTicket.show()
            tgTitleDate.show()
            tgValueDate.show()
            tgTitleSumTicket.show()
            tgValueSumTicket.show()
            tgTitleRedeem.show()
            btnRedeem.show()
            tfRedeem.show()
        }
    }

    private fun hideMainLayout() {
        binding?.run {
            tgTitleBooker.hide()
            tgTitleBookerName.hide()
            tgValueBookerName.hide()
            tgTitleDetailProduct.hide()
            tgTitleProduct.hide()
            tgValueProduct.hide()
            tgTitleTypeTicket.hide()
            tgValueTypeTicket.hide()
            tgTitleDate.hide()
            tgValueDate.hide()
            tgTitleSumTicket.hide()
            tgValueSumTicket.hide()
            tgTitleRedeem.hide()
            btnRedeem.hide()
            tfRedeem.hide()
        }
    }

    private fun showRedeemLayout() {
        binding?.run {
            containerSuccessRedeem.show()
            btnScan.show()
            tgTitleRedeem.hide()
            btnRedeem.hide()
            tfRedeem.hide()
        }
    }

    private fun showVisitorLayout() {
        binding?.run {
            rvDetailVisitor.show()
            tgDetailVisitor.show()
        }
    }

    private fun hideVisitorLayout() {
        binding?.run {
            rvDetailVisitor.hide()
            tgDetailVisitor.hide()
        }
    }

    private fun hideRedeemLayout() {
        binding?.run {
            containerSuccessRedeem.hide()
            btnScan.hide()
        }
    }

    private fun renderMainLayout(redeem: Data) {
        binding?.run {
            tgValueBookerName.text = redeem.user.name
            tgValueProduct.text = redeem.product.displayName
            tgValueTypeTicket.text = redeem.schedule.name
            tgValueDate.text = redeem.schedule.showData
            tgValueSumTicket.text = redeem.quantity.toString()
            renderRedeemLayout(redeem)
        }
    }

    private fun renderVisitorLayout(participants: List<ParticipantDetail>) {
        binding?.run {
            val adapter = EventRedeemVisitorAdapter()
            rvDetailVisitor.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvDetailVisitor.adapter = adapter
            adapter.addParticipant(participants)
        }
    }

    /***
     * Redemption is null or empty or one will have first condition
     * * * Empty or null redemption will hit old redeem
     * * * One Redemption will hit new redeem
     * Multiple Redeem will have second condition
     **/
    private fun renderRedeemLayout(redeem: Data) {
        binding?.run {
            if (isEmptyParticipant(getUpdateListRedemption()) || isOneParticipant(getUpdateListRedemption())) {
                tgTitleRedeem.hide()
                tfRedeem.hide()
                btnRedeem.run {
                    val btnParamRedeem = layoutParams as ConstraintLayout.LayoutParams
                    btnParamRedeem.leftToRight = ConstraintLayout.LayoutParams.UNSET
                    btnParamRedeem.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams = btnParamRedeem

                    text = context.resources.getString(redeemString.ent_redeem_revamp_redeem_title)

                    setOnClickListener {
                        if (isEmptyParticipant(getUpdateListRedemption())) {
                            processOldRedeem()
                        } else if (isOneParticipant(getUpdateListRedemption())){
                            getOneRedemptionPair(getUpdateListRedemption())?.let {
                                viewModel.updateCheckedIds(listOf(it))
                                processRedeem()
                            }
                        }
                    }
                    setMargin(
                        getDimens(dimenUnify.unify_space_16),
                        getDimens(dimenUnify.unify_space_16),
                        getDimens(dimenUnify.unify_space_16),
                        getDimens(dimenUnify.unify_space_0)
                    )
                }

                if (isOneParticipant(getUpdateListRedemption()) &&
                    getListParticipantDetails(getUpdateListRedemption()).isNotEmpty()) {
                    showVisitorLayout()
                    renderVisitorLayout(getListParticipantDetails(getUpdateListRedemption()))
                } else {
                    hideVisitorLayout()
                }
            } else if (isStatusNotAllDisabled(getUpdateListRedemption())) {
                hideVisitorLayout()
                tfRedeem.addOnFocusChangeListener = { _, hasFocus ->
                    if (hasFocus) {
                        showBottomSheet(redeem.schedule.name)
                    }
                }
                btnRedeem.setOnClickListener {
                    processRedeem()
                }
            }
        }
    }

    /***
     * isFromScan mean get the redeem success from start
     * if got isFromScan will show all redeemen without quantity showing and will show time redeemed
     **/
    private fun renderSuccessRedeemLayout(isFromScan: Boolean, qty: Int, date: String) {
        binding?.run {
            context?.let { context ->
                tgSuccessRedeem.text =
                    if (isFromScan) {
                        context.resources.getString(
                            redeemString.ent_redeem_success_all_already_redeem
                        )
                    } else {
                        context.resources.getString(
                            redeemString.ent_redeem_success_redeem,
                            qty
                        )
                    }
                if (isFromScan) {
                    tgSuccessRedeemTime.show()
                    tgSuccessRedeemTime.text = getAllRedeemedTime(context, date)
                } else {
                    tgSuccessRedeemTime.hide()
                }
                btnScan.setOnClickListener {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.QR_SCANNEER)
                    activity?.finish()
                }
            }
        }
    }

    private fun showBottomSheet(mainTitle: String) {
        context?.let { context ->
            val mappedParticipant = participantToVisitableMapper(mainTitle, getUpdateListRedemption(), context)
            val bottomSheetEventRedeem = EventRedeemRevampBottomSheet.getInstance()
            bottomSheetEventRedeem.setListener(this)
            bottomSheetEventRedeem.setList(mappedParticipant)
            bottomSheetEventRedeem.show(
                childFragmentManager,
                EventRedeemRevampBottomSheet::class.java.simpleName
            )
        }
    }

    private fun clearFocusEditText() {
        binding?.tfRedeem?.textInputLayout?.editText?.clearFocus()
    }

    private fun updateSize() {
        if (viewModel.getCheckedIdsSize().isMoreThanZero()) {
            resetErrorInput()
            binding?.tfRedeem?.textInputLayout?.editText?.setText(
                viewModel.getCheckedIdsSize().toString()
            )
        } else {
            binding?.tfRedeem?.textInputLayout?.editText?.setText("")
        }
    }

    private fun setErrorInput() {
        context?.let { context ->
            binding?.tfRedeem?.isInputError = true
            binding?.tfRedeem?.setMessage(context.resources.getString(redeemString.ent_redeem_revamp_sum_product_error))
        }
    }

    private fun resetErrorInput() {
        binding?.tfRedeem?.isInputError = false
        binding?.tfRedeem?.setMessage("")
    }

    private fun showErrorToaster(errorMessage: String?) {
        hideLoading()
        view?.let { view ->
            errorMessage?.let {
                Toaster.build(view, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        }
    }

    override fun onClickSave(listCheckedIds: List<Pair<String, Boolean>>) {
        viewModel.updateCheckedIds(listCheckedIds)
        updateSize()
    }

    override fun onClose() {
        clearFocusEditText()
    }

    companion object {
        fun newInstance(urlRedeem: String) = EventRedeemRevampFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_REDEEM, urlRedeem)
            }
        }
    }
}
