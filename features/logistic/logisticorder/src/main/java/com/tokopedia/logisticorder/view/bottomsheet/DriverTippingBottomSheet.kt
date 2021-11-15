package com.tokopedia.logisticorder.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.BottomsheetTippingGojekBinding
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent
import com.tokopedia.logisticorder.di.TrackingPageComponent
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.utils.TrackingPageUtil.EXTRA_ORDER_ID
import com.tokopedia.logisticorder.utils.TrackingPageUtil.EXTRA_TRACKING_DATA_MODEL
import com.tokopedia.logisticorder.view.TrackingPageViewModel
import com.tokopedia.logisticorder.view.adapter.TippingValueAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class DriverTippingBottomSheet: BottomSheetUnify(), HasComponent<TrackingPageComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<BottomsheetTippingGojekBinding>()
    private val viewModel: TrackingPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TrackingPageViewModel::class.java]
    }

    private var orderId: String? = ""
    private var trackingDataModel: TrackingDataModel? = null
    private lateinit var tippingValueAdapter: TippingValueAdapter

    init {
        setOnDismissListener {
            dismiss()
        }

        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(EXTRA_ORDER_ID)
            trackingDataModel = it.getParcelable(EXTRA_TRACKING_DATA_MODEL)
        }
        iniInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialViewState()
        initObserver()
    }

   private fun iniInjector() {
       component.inject(this)
   }

    private fun initChildLayout() {
        binding = BottomsheetTippingGojekBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    private fun setInitialViewState() {
        setTitle("")
        binding.progressBar.visibility = View.VISIBLE
        binding.paymentTippingLayout.visibility = View.GONE
        binding.resultTippingLayout.visibility = View.GONE
        viewModel.getDriverTipsData("167021704")
    }

    override fun getComponent(): TrackingPageComponent {
        return DaggerTrackingPageComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }


    private fun initObserver() {
        viewModel.driverTipData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    setDriverTipLayout(it.data)
                }

                is Fail -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setDriverTipLayout(logisticDriverModel: LogisticDriverModel) {
        when (logisticDriverModel.status) {
            100 -> {
                //driver found
                setTitle("Beri Tip untuk Driver")
                binding.paymentTippingLayout.visibility = View.VISIBLE
                binding.resultTippingLayout.visibility = View.GONE
                binding.btnTipping.text = "Pilih Pembayaran"

                val chipsLayoutManagerTipping = ChipsLayoutManager.newBuilder(binding.root.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()

                ViewCompat.setLayoutDirection(binding.rvChipsTip, ViewCompat.LAYOUT_DIRECTION_LTR)
                tippingValueAdapter = TippingValueAdapter()
                tippingValueAdapter.tippingValueList = logisticDriverModel.prepayment.presetAmount.toMutableList()

                binding.rvChipsTip.apply {
                    layoutManager = chipsLayoutManagerTipping
                    adapter = tippingValueAdapter
                }


                binding.tickerTippingGojek.apply {
                    setHtmlDescription(String.format(getString(R.string.driver_tipping_ticker_new), logisticDriverModel.prepayment.info.first(), logisticDriverModel.prepayment.info.last()))
                }
            }
            150 -> {
                //tip sedang diproses
            }
            200 -> {
                //post payment
                binding.resultTippingLayout.visibility = View.VISIBLE
                binding.paymentTippingLayout.visibility = View.GONE
                binding.btnTipping.text = "Mengerti"

                binding.apply {
                    imgTipDriver.setImage(R.drawable.ic_succes_tipping_gojek, 0F)
                    tvTipResult.text = "Tip kamu sudah diberikan!"
                    tvTipResultDesc.text = "Tip 100% ditransfer ke driver setelah pesanan sampai. Tip akan dikembalikan ke kamu jika pesanan batal"
                    tvResiValue.text = trackingDataModel?.trackOrder?.shippingRefNum
                    tvDriverNameValue.text = logisticDriverModel.lastDriver.name
                    tvPhoneNumberValue.text = logisticDriverModel.lastDriver.phone
                    tvLicenseValue.text = logisticDriverModel.lastDriver.licenseNumber

                    chipsPayment.chip_image_icon.setImageUrl(logisticDriverModel.payment.methodIcon)
                    chipsPayment.chipText = String.format(getString(R.string.payment_value), logisticDriverModel.payment.method, logisticDriverModel.payment.amountFormatted)
                }

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(orderId: String?, data: TrackingDataModel): DriverTippingBottomSheet {
            return DriverTippingBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ORDER_ID, orderId)
                    putParcelable(EXTRA_TRACKING_DATA_MODEL, data)
                }
            }
        }
    }
}