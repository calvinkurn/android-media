package com.tokopedia.common.topupbills.favorite.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.databinding.FragmentSavedNumberBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.favorite.view.adapter.TopupBillsPersoSavedNumTabAdapter
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DualTabSavedNumberFragment: BaseDaggerFragment() {

    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: ArrayList<String>
    private var currentCategoryName = ""
    private var number: String = ""
    private var loyaltyStatus: String = ""
    private var operatorList: HashMap<String, TelcoAttributesOperator> = hashMapOf()
    private var isSwitchChecked: Boolean = false

    private var binding: FragmentSavedNumberBinding? = null
    private var pagerAdapter: TopupBillsPersoSavedNumTabAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val savedNumberViewModel by lazy {
        viewModelFragmentProvider.get(TopupBillsSavedNumberViewModel::class.java) }

    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding?.commonTopupBillsSavedNumSwitcher?.isChecked = (position == POSITION_FAVORITE_NUMBER)
            savedNumberViewModel.refreshSearchBar(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, "")
            number = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            dgCategoryIds = arguments.getStringArrayList(ARG_PARAM_DG_CATEGORY_IDS) ?: arrayListOf()
            currentCategoryName = arguments.getString(ARG_PARAM_CATEGORY_NAME, "")
            isSwitchChecked = arguments.getBoolean(ARG_PARAM_IS_SWITCH_CHECKED, false)
            loyaltyStatus = arguments.getString(ARG_PARAM_LOYALTY_STATUS, "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    override fun getScreenName(): String {
        return DualTabSavedNumberFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initListener()
        observeData()
    }

    private fun observeData() {
        savedNumberViewModel.clueVisibility.observe(viewLifecycleOwner, { isVisible ->
            if (isVisible) {
                binding?.commonTopupBillsFavoriteNumberClue?.show()
            } else {
                binding?.commonTopupBillsFavoriteNumberClue?.hide()
            }
        })

        savedNumberViewModel.enableSearchBar.observe(viewLifecycleOwner, { isEnable ->
            if (isEnable) {
                binding?.commonTopupBillsSavedNumSearchbar?.show()
            } else {
                binding?.commonTopupBillsSavedNumSearchbar?.hide()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.commonTopupBillsSavedNumSearchbar?.searchBarTextField?.removeTextChangedListener(
            getSearchTextWatcher
        )
    }

    private fun initViewPager() {
        pagerAdapter = TopupBillsPersoSavedNumTabAdapter(
            this, clientNumberType, number,
            dgCategoryIds, currentCategoryName, loyaltyStatus)
        binding?.commonTopupBillsSavedNumViewpager?.run {
            adapter = pagerAdapter
            registerOnPageChangeCallback(viewPagerCallback)
        }
        if (isSwitchChecked) {
            binding?.run {
                commonTopupBillsSavedNumViewpager.currentItem =
                    TopupBillsPersoSavedNumTabAdapter.POSITION_FAVORITE_NUMBER
                commonTopupBillsSavedNumSwitcher.isChecked = isSwitchChecked
            }
        }
    }

    private fun initListener() {
        binding?.run {
            commonTopupBillsSavedNumSwitcher.setOnCheckedChangeListener { _, isChecked ->
                isSwitchChecked = isChecked
                if (isChecked) {
                    commonTopupBillsSavedNumViewpager.setCurrentItem(
                        POSITION_FAVORITE_NUMBER, true
                    )
                } else {
                    commonTopupBillsSavedNumViewpager.setCurrentItem(
                        POSITION_CONTACT_LIST, true
                    )
                }
            }
            commonTopupBillsSavedNumSearchbar.searchBarTextField.addTextChangedListener(
                getSearchTextWatcher
            )
        }
    }

    private fun saveTelcoOperator(rechargeCatalogPrefixSelect: RechargeCatalogPrefixSelect) {
        val operatorList = HashMap<String, TelcoAttributesOperator>()

        rechargeCatalogPrefixSelect.prefixes.forEach {
            if (!operatorList.containsKey(it.operator.id)) {
                operatorList[it.operator.id] = it.operator.attributes
            }
        }

        this.operatorList = operatorList
    }

    private val getSearchTextWatcher = object : android.text.TextWatcher {
        override fun afterTextChanged(text: android.text.Editable?) {
            text?.let {
                savedNumberViewModel.setSearchKeyword(text.toString())
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    override fun onDestroy() {
        binding?.commonTopupBillsSavedNumViewpager?.run {
            adapter = null
            unregisterOnPageChangeCallback(viewPagerCallback)
        }
        super.onDestroy()
    }

    companion object {
        fun newInstance(
            clientNumberType: String, number: String,
            categoryName: String, digitalCategoryIds: ArrayList<String>,
            isSwitchChecked: Boolean, loyaltyStatus: String
        ): Fragment {

            val fragment = DualTabSavedNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, number)
            bundle.putString(ARG_PARAM_CATEGORY_NAME, categoryName.lowercase())
            bundle.putString(ARG_PARAM_LOYALTY_STATUS, loyaltyStatus)
            bundle.putBoolean(ARG_PARAM_IS_SWITCH_CHECKED, isSwitchChecked)
            bundle.putStringArrayList(ARG_PARAM_DG_CATEGORY_IDS, digitalCategoryIds)
            fragment.arguments = bundle
            return fragment
        }

        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE = "ARG_PARAM_EXTRA_CLIENT_NUMBER"
        const val ARG_PARAM_DG_CATEGORY_IDS = "ARG_PARAM_DG_CATEGORY_IDS"
        const val ARG_PARAM_CATEGORY_NAME = "ARG_PARAM_CATEGORY_NAME"
        const val ARG_PARAM_IS_SWITCH_CHECKED = "ARG_PARAM_IS_SWITCH_CHECKED"
        const val ARG_PARAM_LOYALTY_STATUS = "ARG_PARAM_LOYALTY_STATUS"

        const val POSITION_CONTACT_LIST = 0
        const val POSITION_FAVORITE_NUMBER = 1
    }
}