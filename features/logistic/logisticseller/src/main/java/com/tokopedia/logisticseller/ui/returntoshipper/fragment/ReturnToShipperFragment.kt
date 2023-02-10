package com.tokopedia.logisticseller.ui.returntoshipper.fragment

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import com.tokopedia.logisticseller.databinding.FragmentReturnToShipperBinding
import com.tokopedia.logisticseller.di.returntoshipper.DaggerReturnToShipperComponent
import com.tokopedia.logisticseller.di.returntoshipper.ReturnToShipperComponent
import com.tokopedia.logisticseller.ui.returntoshipper.dialog.ReturnToShipperDialog
import com.tokopedia.logisticseller.ui.returntoshipper.uimodel.ReturnToShipperState
import com.tokopedia.logisticseller.ui.returntoshipper.viewmodel.ReturnToShipperViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReturnToShipperFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentReturnToShipperBinding>()

    @Inject
    lateinit var userSession: UserSessionInterface

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
                is ReturnToShipperState.ShowRtsConfirmDialog -> openRTSConfirmationDialog(it.data)
                is ReturnToShipperState.ShowRtsSuccessDialog -> showSuccessDialog()
                is ReturnToShipperState.ShowRtsFailedDialog -> showFailedRtsDialog()
                is ReturnToShipperState.ShowToaster -> showToasterAndFinish(it.errorMessage)
                is ReturnToShipperState.ShowLoading ->
                    binding?.loaderRts?.isVisible =
                        it.isShowLoading
            }
        }
    }

    private fun showToasterAndFinish(errorMessage: String) {
        showToaster(errorMessage)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(DELAY_SHOWING_TOASTER)
            doFinishActivity(Activity.RESULT_FIRST_USER)
        }
    }

    private fun openRTSConfirmationDialog(data: GetGeneralInfoRtsResponse.GeneralInfoRtsData) {
        activity?.apply {
            ReturnToShipperDialog(this).apply {
                showRtsConfirmationDialog(
                    data = data.setUrlImage(),
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

    private fun GetGeneralInfoRtsResponse.GeneralInfoRtsData.setUrlImage(): GetGeneralInfoRtsResponse.GeneralInfoRtsData {
        return if (image.imageId?.isNotBlank() == true) {
            this.apply {
                image.apply {
                    accessToken = userSession.accessToken
                    urlImage = LogisticImageDeliveryHelper.getDeliveryImage(
                        imageId = image.imageId.orEmpty(),
                        orderId = orderId.toLongOrZero(),
                        size = LogisticImageDeliveryHelper.IMAGE_LARGE_SIZE,
                        userId = userSession.userId,
                        osType = LogisticImageDeliveryHelper.DEFAULT_OS_TYPE,
                        deviceId = userSession.deviceId
                    )
                }
            }
        } else {
            this
        }
    }

    private fun showSuccessDialog() {
        activity?.apply {
            ReturnToShipperDialog(this).apply {
                showRtsSuccessDialog {
                    doFinishActivity(Activity.RESULT_OK)
                }
            }
        }
    }

    private fun showFailedRtsDialog() {
        activity?.apply {
            ReturnToShipperDialog(this).apply {
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

    override fun onResume() {
        super.onResume()

        val window = activity?.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        private const val DELAY_SHOWING_TOASTER = 3000L

        fun newInstance(orderId: String): ReturnToShipperFragment {
            return ReturnToShipperFragment().apply {
                arguments = Bundle().apply {
                    putString(LogisticSellerConst.PARAM_ORDER_ID, orderId)
                }
            }
        }
    }
}
