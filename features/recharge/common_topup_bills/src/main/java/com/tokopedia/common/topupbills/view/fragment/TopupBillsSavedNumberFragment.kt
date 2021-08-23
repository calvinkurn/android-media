package com.tokopedia.common.topupbills.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.databinding.FragmentSavedNumberBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.adapter.TopupBillsSavedNumTabAdapter
import com.tokopedia.common.topupbills.view.listener.SavedNumberSearchListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TopupBillsSavedNumberFragment: BaseDaggerFragment() {

    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: ArrayList<String>
    private var currentCategoryName = ""
    private var number: String = ""
    private var operatorData: TelcoCatalogPrefixSelect? = null
    private var operatorList: HashMap<String, TelcoAttributesOperator> = hashMapOf()

    private var binding: FragmentSavedNumberBinding? = null
    private var pagerAdapter: TopupBillsSavedNumTabAdapter? = null

    @Suppress("UNCHECKED_CAST")
    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, "")
            number = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            dgCategoryIds = arguments.getStringArrayList(ARG_PARAM_DG_CATEGORY_IDS) ?: arrayListOf()
            operatorData = arguments.getParcelable(ARG_PARAM_CATALOG_PREFIX_SELECT)
            currentCategoryName = arguments.getString(ARG_PARAM_CATEGORY_NAME, "")
        }

        operatorData?.rechargeCatalogPrefixSelect?.let { saveTelcoOperator(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    override fun getScreenName(): String {
        return TopupBillsSavedNumberFragment::class.java.simpleName
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
    }

    // TODO: [Misael] nanti check bener ga panggilnya dari sini si removal
    override fun onDestroyView() {
        super.onDestroyView()
        binding?.commonTopupBillsSavedNumSearchbar?.searchBarTextField?.removeTextChangedListener(
            getSearchTextWatcher
        )
    }

    private fun initViewPager() {
        pagerAdapter = TopupBillsSavedNumTabAdapter(
            this, clientNumberType, number,
            dgCategoryIds, currentCategoryName, operatorData,
        )
        binding?.commonTopupBillsSavedNumViewpager?.adapter = pagerAdapter
    }

    private fun initListener() {
        binding?.run {
            commonTopupBillsSavedNumSwitcher.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    commonTopupBillsSavedNumViewpager.setCurrentItem(
                        POSITION_FAVORITE_NUMBER, true)
                } else {
                    commonTopupBillsSavedNumViewpager.setCurrentItem(
                        POSITION_CONTACT_LIST, true)
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
                val pos = binding?.commonTopupBillsSavedNumViewpager?.currentItem
                pos?.let {
                    val fragment = pagerAdapter?.createFragment(it) as SavedNumberSearchListener
                    fragment.filterData(text.toString())
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    companion object {
        fun newInstance(
            clientNumberType: String, number: String,
            operatorData: TelcoCatalogPrefixSelect?,
            categoryName: String, digitalCategoryIds: ArrayList<String>
        ): Fragment {

            val fragment = TopupBillsSavedNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, number)
            bundle.putString(ARG_PARAM_CATEGORY_NAME, categoryName.toLowerCase(
                    Locale.getDefault()))
            bundle.putStringArrayList(ARG_PARAM_DG_CATEGORY_IDS, digitalCategoryIds)
            bundle.putParcelable(ARG_PARAM_CATALOG_PREFIX_SELECT, operatorData)
            fragment.arguments = bundle
            return fragment
        }

        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE = "ARG_PARAM_EXTRA_CLIENT_NUMBER"
        const val ARG_PARAM_CATALOG_PREFIX_SELECT = "ARG_PARAM_CATALOG_PREFIX_SELECT"
        const val ARG_PARAM_DG_CATEGORY_IDS = "ARG_PARAM_DG_CATEGORY_IDS"
        const val ARG_PARAM_CATEGORY_NAME = "ARG_PARAM_CATEGORY_NAME"

        const val POSITION_CONTACT_LIST = 0
        const val POSITION_FAVORITE_NUMBER = 1
    }
}