package com.tokopedia.entertainment.pdp.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.entertainment.databinding.FragmentEventRedeemRevampBinding
import com.tokopedia.entertainment.pdp.activity.EventRedeemActivity.Companion.EXTRA_URL_REDEEM
import com.tokopedia.entertainment.pdp.bottomsheet.EventRedeemRevampBottomSheet
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper.participantToVisitableMapper
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Data
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Participant
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventRedeemRevampViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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
    }

    private fun initalRequest() {
        hideMainLayout()
        hideRedeemLayout()
        hideGlobalError()
        if (!userSession.isLoggedIn) {
            showGlobalError(isNotLogin = true, isUrlEmpty = false, null)
        } else if (urlRedeem == "") {
            showGlobalError(isNotLogin = false, isUrlEmpty = true, null)
        } else if (userSession.isLoggedIn && urlRedeem != "") {
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
                        showSuccessRedeem()
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
                    context, com.tokopedia.unifyprinciples.R.color.Unify_NN950
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

    private fun showData(redeem: Data) {
        hideLoading()
        hideGlobalError()
        hideRedeemLayout()
        showMainLayout()
        renderMainLayout(redeem)
    }

    private fun showSuccessRedeem() {
        hideLoading()
        hideGlobalError()
        showMainLayout()
        showRedeemLayout()
        renderSuccessRedeemLayout()
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
            tfRedeem.addOnFocusChangeListener = { _, hasFocus ->
                if (hasFocus) {
                    showBottomSheet()
                }
            }
            btnRedeem.setOnClickListener {
                processRedeem()
            }
        }
    }

    private fun renderSuccessRedeemLayout() {
        binding?.run {
            context?.let { context ->
                tgSuccessRedeem.text = context.resources.getString(redeemString.ent_redeem_success_redeem, viewModel.getCheckedIdsSize())
                btnScan.setOnClickListener {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.QR_SCANNEER)
                }
            }
        }
    }

    private fun showBottomSheet() {
        context?.let { context ->
            val mappedParticipant = participantToVisitableMapper(getUpdateListRedemption(), context)
            val bottomSheetEventRedeem = EventRedeemRevampBottomSheet.getInstance()
            bottomSheetEventRedeem.setListener(this)
            bottomSheetEventRedeem.setList(mappedParticipant)
            bottomSheetEventRedeem.show(
                childFragmentManager,
                EventPDPComponent::class.java.simpleName
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
        context?.let { context ->
            binding?.tfRedeem?.isInputError = false
            binding?.tfRedeem?.setMessage("")
        }
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
