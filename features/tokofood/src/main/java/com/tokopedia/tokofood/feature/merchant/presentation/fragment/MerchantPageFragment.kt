package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.di.TokoFoodComponentBuilder
import com.tokopedia.tokofood.databinding.FragmentMerchantPageLayoutBinding
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.MerchantPageCarouselAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.MerchantInfoBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_FULL
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class MerchantPageFragment : BaseMultiFragment(), MerchantCarouseItemViewHolder.OnCarouselItemClickListener {

    private var binding: FragmentMerchantPageLayoutBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(MerchantPageViewModel::class.java)
    }

    private var merchantInfoBottomSheet: MerchantInfoBottomSheet? = null
    private var adapter: MerchantPageCarouselAdapter? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initInjector()
    }

    private fun initInjector() {
        val baseMainApplication = requireContext().applicationContext as BaseMainApplication
        DaggerMerchantPageComponent.builder()
                .tokoFoodComponent(TokoFoodComponentBuilder.getComponent(baseMainApplication))
                .build()
                .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_merchant_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                Toast.makeText(requireContext(), "click on share", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_open_global_menu -> {
                RouteManager.route(requireContext(), ApplinkConst.HOME_NAVIGATION)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentMerchantPageLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        setupMerchantLogo()
        setupMerchantProfileCarousel()

        observeLiveData()
        viewModel.getMerchantData("", "", "")

    }

    private fun observeLiveData() {
        viewModel.getMerchantDataResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val merchantData = result.data.tokofoodGetMerchantData
                    // render ticker data if not empty
                    val tickerData = merchantData.ticker
                    if (!viewModel.isTickerDetailEmpty(tickerData)) {
                        renderTicker(tickerData)
                    }
                    // render merchant logo, name, categories, carousel
                    val merchantProfile = merchantData.merchantProfile
                    renderMerchantProfile(merchantProfile)
                    // setup merchant info bottom sheet
                    val name = merchantProfile.name
                    val address = merchantProfile.address
                    val merchantOpsHours = viewModel.mapOpsHourDetailsToMerchantOpsHours(merchantProfile.opsHourDetail)
                    setupMerchantInfoBottomSheet(name, address, merchantOpsHours)

                }
                is Fail -> {

                }
            }
        })
    }

    private fun setupMerchantLogo() {
        binding?.iuMerchantLogo?.type = ImageUnify.TYPE_CIRCLE
    }

    private fun setupMerchantProfileCarousel() {
        adapter = MerchantPageCarouselAdapter(this)
        binding?.rvMerchantInfoCarousel?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
        }
    }

    private fun setupProductList() {

    }

    private fun renderTicker(tickerData: TokoFoodTickerDetail) {
        binding?.tickerMerchantPage?.apply {
            this.tickerType = TYPE_WARNING
            this.tickerShape = SHAPE_FULL
            this.closeButtonVisibility = View.GONE
            this.tickerTitle = tickerTitle
            this.setTextDescription(tickerData.subtitle)
            this.show()
        }
    }

    private fun renderMerchantProfile(merchantProfile: TokoFoodMerchantProfile) {
        val imageUrl = merchantProfile.imageURL ?: ""
        if (imageUrl.isNotBlank()) binding?.iuMerchantLogo?.setImageUrl(imageUrl)
        binding?.tpgMerchantName?.text = merchantProfile.name
        binding?.tpgMerchantCategory?.text = merchantProfile.merchantCategories.joinToString()
        val carouselData = viewModel.mapMerchantProfileToCarouselData(merchantProfile)
        adapter?.setCarouselData(carouselData)
        // TODO: add close in xxx
    }

    private fun setupMerchantInfoBottomSheet(name: String, address: String, merchantOpsHours: List<MerchantOpsHour>) {
        merchantInfoBottomSheet = MerchantInfoBottomSheet.createInstance(name, address, merchantOpsHours)
    }

    override fun onCarouselItemClicked() {
        merchantInfoBottomSheet?.show(childFragmentManager)
    }
}