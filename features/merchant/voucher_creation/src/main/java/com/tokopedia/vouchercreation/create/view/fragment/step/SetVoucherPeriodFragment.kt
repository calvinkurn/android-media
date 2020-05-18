package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
import com.tokopedia.vouchercreation.create.view.viewmodel.SetVoucherPeriodViewModel
import kotlinx.android.synthetic.main.mvc_set_voucher_period_fragment.*
import javax.inject.Inject

class SetVoucherPeriodFragment(private val onNext: (String, String, String, String) -> Unit,
                               private val getVoucherBanner: () -> BannerVoucherUiModel,
                               private val getBannerBaseUiModel: () -> BannerBaseUiModel) : Fragment() {

    companion object {
        @JvmStatic
        fun createInstance(onNext: (String, String, String, String) -> Unit,
                           getVoucherBanner: () -> BannerVoucherUiModel,
                           getBannerBaseUiModel: () -> BannerBaseUiModel) = SetVoucherPeriodFragment(onNext, getVoucherBanner, getBannerBaseUiModel)

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(SetVoucherPeriodViewModel::class.java)
    }

    private var bannerVoucherUiModel: BannerVoucherUiModel = getVoucherBanner()

    private var bannerBitmap: Bitmap? = null

    private var isWaitingForValidation = false

    private var dummyStartDate = "2020-06-01"
    private var dummyEndDate = "2020-06-30"
    private var dummyStartHour = "12:00"
    private var dummyEndHour = "12:00"

    override fun onResume() {
        super.onResume()
        bannerVoucherUiModel = getVoucherBanner()
        drawBanner()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mvc_set_voucher_period_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        observeLiveData()
        disableTextFieldEdit()
        setDateNextButton?.run {
            setOnClickListener {
                isLoading = true
                onNextClicked()
            }
        }
        setDummyDate()
    }

    private fun onSuccessGetBitmap(bitmap: Bitmap) {
        activity?.runOnUiThread {
            periodBannerImage?.setImageBitmap(bitmap)
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.observe(viewModel.periodValidationLiveData) { result ->
            if (isWaitingForValidation) {
                when(result) {
                    is Success -> {
                        val validation = result.data
                        if (!validation.getIsHaveError()) {
                            onNext(dummyStartDate, dummyStartHour, dummyEndDate, dummyEndHour)
                        } else {
                            if (validation.dateStartError.isNotBlank() && validation.hourStartError.isNotBlank()) {
                                startDateTextField?.run {
                                    setError(true)
                                    setMessage(validation.dateStartError)
                                }
                            }
                            if (validation.dateEndError.isNotBlank() && validation.hourEndError.isNotBlank()) {
                                endDateTextField?.run {
                                    setError(true)
                                    setMessage(validation.dateEndError)
                                }
                            }
                        }
                    }
                    is Fail -> {
                        val error = result.throwable.message.toBlankOrString()
                        view?.showErrorToaster(error)
                    }
                }
                isWaitingForValidation = false
                setDateNextButton?.isLoading = false
            }
        }
    }

    private fun disableTextFieldEdit() {
        startDateTextField?.textFieldInput?.run {
            isEnabled = false
            inputType = InputType.TYPE_NULL
        }
        endDateTextField?.textFieldInput?.run {
            isEnabled = false
            inputType = InputType.TYPE_NULL
        }
    }

    private fun setDummyDate() {
        viewModel.setStartPeriod(dummyStartDate, dummyStartHour)
        viewModel.setEndPeriod(dummyEndDate, dummyEndHour)
        startDateTextField?.textFieldInput?.setText("Kam, 01 Juni 2020, 12:00 WIB")
        endDateTextField?.textFieldInput?.setText("Sel, 30 Juni 2020, 12:00 WIB")
    }

    private fun drawBanner() {
        if (bannerBitmap == null) {
            context?.run {
                Glide.with(this)
                        .asDrawable()
                        .load(BANNER_BASE_URL)
                        .signature(ObjectKey(System.currentTimeMillis().toString()))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                activity?.runOnUiThread {
                                    val bitmap = resource.toBitmap()
                                    val painter = VoucherPreviewPainter(this@run, bitmap, ::onSuccessGetBitmap, getBannerBaseUiModel())
                                    painter.drawFull(bannerVoucherUiModel, bitmap)
                                }
                                return false
                            }
                        })
                        .submit()
            }
        }
    }

    private fun onNextClicked() {
        isWaitingForValidation = true
        viewModel.validateVoucherPeriod()
    }

}