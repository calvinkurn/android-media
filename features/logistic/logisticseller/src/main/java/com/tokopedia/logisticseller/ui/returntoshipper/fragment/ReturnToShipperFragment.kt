package com.tokopedia.logisticseller.ui.returntoshipper.fragment

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import com.tokopedia.logisticseller.databinding.FragmentReturnToShipperBinding
import com.tokopedia.logisticseller.di.returntoshipper.DaggerReturnToShipperComponent
import com.tokopedia.logisticseller.di.returntoshipper.ReturnToShipperComponent
import com.tokopedia.logisticseller.ui.returntoshipper.dialog.ReturtToShipperDialog
import com.tokopedia.logisticseller.ui.returntoshipper.uimodel.ReturnToShipperState
import com.tokopedia.logisticseller.ui.returntoshipper.viewmodel.ReturnToShipperViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ReturnToShipperFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentReturnToShipperBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ReturnToShipperViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ReturnToShipperViewModel::class.java)
    }

    private var orderId: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: ReturnToShipperComponent = DaggerReturnToShipperComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(LogisticSellerConst.PARAM_ORDER_ID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReturnToShipperBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        viewModel.getGeneralInformation(orderId)
    }

    private fun initObserver() {
        viewModel.confirmationRtsState.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ReturnToShipperState.ShowRtsConfirmDialog -> openReturnToShipperConfirmationDialog(
                    it.data
                )
                is ReturnToShipperState.ShowRtsSuccessDialog -> showSuccessDialog()
                is ReturnToShipperState.ShowRtsFailedDialog -> showFailedRtsDialog()
                is ReturnToShipperState.ShowToaster -> {
                    showToaster(it.errorMessage)
                    doFinishActivity()
                }
                is ReturnToShipperState.ShowLoading -> binding?.loaderRts?.isVisible =
                    it.isShowLoading
            }
        }
    }

    private fun openReturnToShipperConfirmationDialog(data: GetGeneralInfoRtsResponse.GeneralInfoRtsData) {
        activity?.apply {
            ReturtToShipperDialog(this).apply {
                showRtsConfirmationDialog(
                    data = data,
                    onPrimaryCTAClickListener = {
                        viewModel.requestGeneralInformation(
                            orderId = orderId,
                            action = GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION
                        )
                    },
                    onSecondaryCTAClickListener = {
                        viewModel.requestGeneralInformation(
                            orderId = orderId,
                            action = GeneralInfoRtsParam.ACTION_RTS_HELPER
                        )
                        openWebview(data.articleUrl)
                    },
                    onDismissListener = {
                        doFinishActivity()
                    }
                )
            }
        }
    }

    private fun showSuccessDialog() {
        activity?.apply {
            ReturtToShipperDialog(this).apply {
                showRtsSuccessDialog {
                    doFinishActivity(Activity.RESULT_OK)
                }
            }
        }
    }

    private fun showFailedRtsDialog() {
        activity?.apply {
            ReturtToShipperDialog(this).apply {
                showRtsFailedDialog {
                    doFinishActivity(Activity.RESULT_FIRST_USER)
                }
            }
        }
    }

    private fun openWebview(url: String) {
        startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, url))
    }

    private fun showToaster(errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun doFinishActivity(result: Int = RESULT_CANCELED) {
        activity?.apply {
            setResult(result, Intent())
            finish()
        }
    }

    companion object {
        fun newInstance(orderId: String): ReturnToShipperFragment {
            return ReturnToShipperFragment().apply {
                arguments = Bundle().apply {
                    putString(LogisticSellerConst.PARAM_ORDER_ID, orderId)
                }
            }
        }
    }
}
