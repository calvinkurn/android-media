package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
import com.tokopedia.vouchercreation.create.view.viewmodel.PromotionBudgetAndTypeViewModel
import kotlinx.android.synthetic.main.mvc_banner_voucher_fragment.*
import javax.inject.Inject

class PromotionBudgetAndTypeFragment(private val onNextStep: () -> Unit = {},
                                     private val setVoucherBitmap: (Bitmap) -> Unit)
    : BaseCreateMerchantVoucherFragment<PromotionTypeBudgetTypeFactory, PromotionTypeBudgetAdapterTypeFactory>(onNextStep, false) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit,
                           setVoucherBitmap: (Bitmap) -> Unit) = PromotionBudgetAndTypeFragment(onNext, setVoucherBitmap)

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override var layoutRes: Int = R.layout.mvc_banner_voucher_fragment

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(PromotionBudgetAndTypeViewModel::class.java)
    }

    private val promotionTypeInputWidget by lazy {
        PromotionTypeInputUiModel()
    }

    private val generalExpensesInfoBottomSheetFragment by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context).apply {
            setTitle(context?.resources?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
        }
    }

    private val bannerVoucherUiModel: BannerVoucherUiModel<PromotionTypeBudgetTypeFactory> =
            // todo: change dummy
            BannerVoucherUiModel(
                    VoucherImageType.FreeDelivery(0),
                    "harusnyadaristep1",
                    "Ini Harusnya dari Backend",
                    "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2020/5/6/1479278/1479278_3bab5e93-003a-4819-a68a-421f69224a59.jpg"
            )

    private var painter: VoucherPreviewPainter? = null

    override var extraWidget: List<Visitable<PromotionTypeBudgetTypeFactory>> =
            listOf(promotionTypeInputWidget)

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {

    }

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {

    }

    override fun onFinishRenderInitial() {

    }

    override fun getAdapterTypeFactory(): PromotionTypeBudgetAdapterTypeFactory =
            PromotionTypeBudgetAdapterTypeFactory(this, onNextStep, ::onShouldChangeBannerValue)

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun setupView() {
        super.setupView()
        setupBottomSheet()
        observeLiveData()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }

    private fun setupBottomSheet() {
        context?.run {
            addBottomSheetView(CreateVoucherBottomSheetType.GENERAL_EXPENSE_INFO, generalExpensesInfoBottomSheetFragment)
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.observe(viewModel.bannerBitmapLiveData) { bitmap ->
            bannerImage?.setImageBitmap(bitmap)
            setVoucherBitmap(bitmap)
        }
    }

    private fun onShouldChangeBannerValue(voucherImageType: VoucherImageType) {
        var canSetBitmap = true
        bannerImage?.run {
            bannerVoucherUiModel.imageType = voucherImageType
            Glide.with(context)
                    .asDrawable()
                    .load(BANNER_BASE_URL)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val bitmap = resource.toBitmap()
                            if (painter == null) {
                                painter = VoucherPreviewPainter(context, bitmap, ::onSuccessGetBanner)
                            }
                            if (canSetBitmap) {
                                canSetBitmap = false
                                painter?.let {
                                    viewModel.drawBanner(it, bannerVoucherUiModel, resource.toBitmap())
                                }
                            }
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun onSuccessGetBanner(bitmap: Bitmap) {
        bannerImage?.setImageBitmap(bitmap)
        setVoucherBitmap(bitmap)
    }
}
