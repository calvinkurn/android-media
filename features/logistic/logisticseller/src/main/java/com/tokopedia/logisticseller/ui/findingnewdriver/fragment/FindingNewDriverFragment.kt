package com.tokopedia.logisticseller.ui.findingnewdriver.fragment

import android.app.Activity.RESULT_FIRST_USER
import android.app.Activity.RESULT_OK
import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.databinding.FragmentFindingNewDriverBinding
import com.tokopedia.logisticseller.di.findingnewdriver.DaggerFindingNewDriverComponent
import com.tokopedia.logisticseller.di.findingnewdriver.FindingNewDriverComponent
import com.tokopedia.logisticseller.ui.findingnewdriver.bottomsheet.FindingNewDriverBottomSheet
import com.tokopedia.logisticseller.ui.findingnewdriver.uimodel.NewDriverBookingState
import com.tokopedia.logisticseller.ui.findingnewdriver.viewmodel.FindingNewDriverViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class FindingNewDriverFragment :
    BaseDaggerFragment(),
    FindingNewDriverBottomSheet.FindingNewDriverListener {

    private var binding by autoClearedNullable<FragmentFindingNewDriverBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FindingNewDriverViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FindingNewDriverViewModel::class.java)
    }

    private var findingNewDriverBottomSheet: FindingNewDriverBottomSheet? = null

    private var isRequestNewDriver = false

    private var orderId: String = ""
    private var invoice: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: FindingNewDriverComponent = DaggerFindingNewDriverComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(LogisticSellerConst.PARAM_ORDER_ID, "")
            invoice = it.getString(LogisticSellerConst.PARAM_INVOICE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindingNewDriverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        openFindingNewDriverBottomSheet()
    }

    private fun initObserver() {
        viewModel.newDriverBooking.observe(viewLifecycleOwner) {
            when (it) {
                is NewDriverBookingState.Success -> showSuccessDialog(it.message)
                is NewDriverBookingState.Fail -> showFailedDialog()
                is NewDriverBookingState.Loading ->
                    binding?.loaderFindingDriver?.isVisible = it.isShowLoading
            }
        }
    }

    private fun showSuccessDialog(description: String) {
        showDialog(
            title = getString(R.string.title_success_find_new_driver),
            description = description,
            primaryCtaText = getString(R.string.btn_understand),
            imageIcon = R.drawable.ic_logisticseller_recshedulepickup_success,
            onDismissListener = {
                doFinishActivity(RESULT_OK)
            }
        )
    }

    private fun showFailedDialog() {
        showDialog(
            title = getString(R.string.title_failed_find_new_driver),
            description = getString(
                R.string.description_failed_find_new_driver,
                invoice
            ).parseAsHtml(),
            primaryCtaText = getString(R.string.btn_understand),
            imageIcon = R.drawable.ic_logisticseller_reschedulepickup_fail,
            onDismissListener = {
                doFinishActivity(RESULT_FIRST_USER)
            }
        )
    }

    private fun showDialog(
        title: String,
        description: CharSequence,
        primaryCtaText: String,
        secondaryCtaText: String? = null,
        onDismissListener: (() -> Unit)? = null,
        imageIcon: Int = 0
    ) {
        context?.apply {
            DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON).apply {
                if (DeviceScreenInfo.isTablet(context)) {
                    dialogMaxWidth = getScreenWidth() / 2
                }
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCtaText)
                setSecondaryCTAText(secondaryCtaText.orEmpty())
                setImageDrawable(imageIcon)
                setPrimaryCTAClickListener {
                    this.dismiss()
                }
                setOnDismissListener {
                    onDismissListener?.invoke()
                }
                show()
            }
        }
    }

    private fun openFindingNewDriverBottomSheet() {
        findingNewDriverBottomSheet = FindingNewDriverBottomSheet.showFindingNewDriverBottomSheet(
            orderId,
            this@FindingNewDriverFragment
        ).apply {
            setOnDismissListener {
                if (isRequestNewDriver.not()) {
                    doFinishActivity()
                }
            }
        }

        activity?.supportFragmentManager?.apply {
            findingNewDriverBottomSheet?.show(this, FindingNewDriverBottomSheet.TAG)
        }
    }

    override fun onClickFindNewDriver() {
        isRequestNewDriver = true
        viewModel.getNewDriverBooking(orderId)
        findingNewDriverBottomSheet?.dismiss()
    }

    override fun onFailedGetNewDriverAvailability() {
        isRequestNewDriver = true
        findingNewDriverBottomSheet?.dismiss()
        showFailedDialog()
    }

    private fun doFinishActivity(resultCode: Int = RESULT_CANCELED) {
        activity?.apply {
            setResult(resultCode, Intent())
            finish()
        }
    }

    companion object {
        fun newInstance(orderId: String, invoice: String): FindingNewDriverFragment {
            return FindingNewDriverFragment().apply {
                arguments = Bundle().apply {
                    putString(LogisticSellerConst.PARAM_ORDER_ID, orderId)
                    putString(LogisticSellerConst.PARAM_INVOICE, invoice)
                }
            }
        }
    }
}
