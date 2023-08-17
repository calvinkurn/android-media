package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsGroupSettingsBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import javax.inject.Inject
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.GROUP_SETTINGS_STATE_CREATE
import com.tokopedia.topads.dashboard.recommendation.common.decoration.ChipsInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.views.activities.ProductRecommendationActivity
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_topads_group_settings.*

class GroupSettingsFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsGroupSettingsBinding? = null

    private val productListAdapter by lazy {
        ProductListAdapter(null, null)
    }

    @Inject
    lateinit var mapper: ProductRecommendationMapper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var currentGroupSettingsState = GROUP_SETTINGS_STATE_CREATE

    private val viewModel: ProductRecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[ProductRecommendationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopadsGroupSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeViews()
        attachClickListener()
    }

    private fun initializeViews() {
        binding?.productsRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding?.productsRv?.addItemDecoration(ChipsInsightItemDecoration())
        binding?.productsRv?.adapter = productListAdapter

        when (val products = viewModel.productItemsLiveData.value) {
            is TopadsProductListState.Success -> {
                val selectedItems =
                    products.data.filter {
                        (it as? ProductItemUiModel)?.isSelected ?: false
                    }
                binding?.featuredProductsCount?.text = String.format(
                    getString(R.string.topads_insight_centre_featured_products),
                    selectedItems.size
                )
                productListAdapter.submitList(
                    mapper.convertProductItemToFeaturedProductsUiModel(
                        selectedItems
                    )
                )
            }
            else -> {}
        }

        setUpCreateGroupPage()

    }

    private fun attachClickListener() {
        binding?.headerGroupSettings?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding?.createNewGroupCta?.setOnClickListener {
            if (currentGroupSettingsState != GROUP_SETTINGS_STATE_CREATE){
                setUpCreateGroupPage()
                currentGroupSettingsState = GROUP_SETTINGS_STATE_CREATE
                binding?.choseGroupCta?.isChecked = false
            }
        }

        binding?.choseGroupCta?.setOnClickListener {
            if (currentGroupSettingsState != GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING){
                setUpChooseGroupPage()
                currentGroupSettingsState = GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING
                binding?.createNewGroupCta?.isChecked = false
            }
        }
    }

    private fun setUpCreateGroupPage(){
        changeChildFragment(CreateNewGroupFragment.createInstance())
    }

    private fun setUpChooseGroupPage(){
//        changeChildFragment()
    }

    private fun changeChildFragment(fragment: Fragment){
        childFragmentManager.popBackStack()
        childFragmentManager.beginTransaction().setCustomAnimations(
            com.tokopedia.abstraction.R.anim.slide_in_right,
            com.tokopedia.abstraction.R.anim.slide_out_left,
            com.tokopedia.abstraction.R.anim.slide_in_left,
            com.tokopedia.abstraction.R.anim.slide_out_right
        ).replace(
            R.id.groupSettingsFragmentContainer,
            fragment
        ).commit()
    }

    companion object {
        fun createInstance(): GroupSettingsFragment {
            return GroupSettingsFragment()
        }
    }

    override fun getScreenName(): String = javaClass.name
    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }
}
