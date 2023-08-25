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
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import javax.inject.Inject
import com.tokopedia.topads.dashboard.R
import com.tokopedia.abstraction.R as abstractionR
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.GROUP_SETTINGS_STATE_CREATE
import com.tokopedia.topads.dashboard.recommendation.common.decoration.ChipsInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.ProductListAdapter

class GroupSettingsFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsGroupSettingsBinding? = null

    private val productListAdapter by lazy {
        ProductListAdapter(null, null)
    }

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

        binding?.featuredProductsCount?.text = String.format(
            getString(R.string.topads_insight_centre_featured_products),
            viewModel.getSelectedProductItems()?.size
        )
        productListAdapter.submitList(
            viewModel.getMapperInstance().convertProductItemToFeaturedProductsUiModel(
                viewModel.getSelectedProductItems()
            )
        )

        setUpCreateGroupPage()

    }

    private fun attachClickListener() {
        binding?.headerGroupSettings?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding?.createNewGroupCta?.setOnClickListener {
            if (currentGroupSettingsState != GROUP_SETTINGS_STATE_CREATE) {
                setUpCreateGroupPage()
                currentGroupSettingsState = GROUP_SETTINGS_STATE_CREATE
                binding?.choseGroupCta?.isChecked = false
            }
        }

        binding?.choseGroupCta?.setOnClickListener {
            if (currentGroupSettingsState != GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING) {
                setUpChooseGroupPage()
                currentGroupSettingsState = GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING
                binding?.createNewGroupCta?.isChecked = false
            }
        }

        binding?.reset?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun setUpCreateGroupPage() {
        changeChildFragment(CreateNewGroupFragment.createInstance())
    }

    private fun setUpChooseGroupPage() {
        changeChildFragment(ChooseGroupFragment.createInstance())
    }

    private fun changeChildFragment(fragment: Fragment) {
        childFragmentManager.popBackStack()
        childFragmentManager.beginTransaction().setCustomAnimations(
            abstractionR.anim.slide_in_right,
            abstractionR.anim.slide_out_left,
            abstractionR.anim.slide_in_left,
            abstractionR.anim.slide_out_right
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
