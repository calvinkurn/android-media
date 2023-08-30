package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val createNewGroupFragment: CreateNewGroupFragment by lazy { CreateNewGroupFragment.createInstance() }
    private val chooseGroupFragment: ChooseGroupFragment by lazy { ChooseGroupFragment.createInstance() }

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
        attachChildFragments()
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
        showCreateGroupPage()
    }

    private fun attachChildFragments() {
        childFragmentManager.beginTransaction().add(
            R.id.groupSettingsFragmentContainer,
            createNewGroupFragment
        ).add(
            R.id.groupSettingsFragmentContainer,
            chooseGroupFragment
        ).commit()
    }

    private fun attachClickListener() {
        binding?.headerGroupSettings?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding?.createNewGroupCta?.setOnClickListener {
            if (currentGroupSettingsState != GROUP_SETTINGS_STATE_CREATE) {
                showCreateGroupPage()
                currentGroupSettingsState = GROUP_SETTINGS_STATE_CREATE
                binding?.choseGroupCta?.isChecked = false
            }
        }

        binding?.choseGroupCta?.setOnClickListener {
            if (currentGroupSettingsState != GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING) {
                showChooseGroupPage()
                currentGroupSettingsState = GROUP_SETTINGS_STATE_CHOOSE_FROM_EXISTING
                binding?.createNewGroupCta?.isChecked = false
            }
        }

        binding?.reset?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun showCreateGroupPage() {
        childFragmentManager.beginTransaction().hide(chooseGroupFragment).commit()
        childFragmentManager.beginTransaction().setCustomAnimations(
            abstractionR.anim.slide_in_right,
            abstractionR.anim.slide_out_left,
            abstractionR.anim.slide_in_left,
            abstractionR.anim.slide_out_right
        ).show(createNewGroupFragment).commit()
    }

    private fun showChooseGroupPage() {
        childFragmentManager.beginTransaction().hide(createNewGroupFragment).commit()
        childFragmentManager.beginTransaction().setCustomAnimations(
            abstractionR.anim.slide_in_right,
            abstractionR.anim.slide_out_left,
            abstractionR.anim.slide_in_left,
            abstractionR.anim.slide_out_right
        ).show(chooseGroupFragment).commit()
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
