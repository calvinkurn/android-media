package com.tokopedia.shop.open.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.data.model.PostalCodeTypeFactory
import com.tokopedia.shop.open.data.model.PostalCodeViewModel
import com.tokopedia.shop.open.view.activity.ShopOpenPostalCodeChooserActivity.Companion.ARGUMENT_DATA_POSTAL_CODE
import com.tokopedia.shop.open.view.adapter.PostalCodeAdapterTypeFactory
import com.tokopedia.shop.open.view.listener.PostalCodeChooserContract
import kotlinx.android.synthetic.main.fragment_shop_open_postal_code_chooser.*

/**
 * Created by Yehezkiel on 22/05/18.
 * Before using this class, Use DistrictRecommendationActivity first to get list of PostalCode
 */
class ShopOpenPostalCodeChooserFragment : BaseSearchListFragment<PostalCodeViewModel,
        PostalCodeTypeFactory>(), PostalCodeChooserContract.View {

    companion object {
        val DEBOUNCE_DELAY_IN_MILIS: Long = 700
        val INTENT_DATA_POSTAL_CODE = "postal_code"
        // Need postal code list
        fun newInstance(postalCode: ArrayList<String>) =
                ShopOpenPostalCodeChooserFragment().also {
                    it.arguments = Bundle().apply {
                        putStringArrayList(ARGUMENT_DATA_POSTAL_CODE, postalCode)
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideMessageSection()
        searchInputView.setDelayTextChanged(DEBOUNCE_DELAY_IN_MILIS)
        loadData(defaultInitialPage)
        searchInputView.closeImageButton.setOnClickListener {
            searchInputView.searchText = ""
        }
        swipe_refresh_layout.isEnabled = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_open_postal_code_chooser, container, false)
    }

    override fun getAdapterTypeFactory(): PostalCodeTypeFactory {
        return PostalCodeAdapterTypeFactory()
    }

    override fun onItemClicked(postalCode: PostalCodeViewModel) {
        if (activity != null){
            val resultIntent = Intent()
            resultIntent.putExtra(INTENT_DATA_POSTAL_CODE, postalCode.postalCode)
            activity?.setResult(Activity.RESULT_OK, resultIntent)
            activity?.finish()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onSearchSubmitted(text: String?) {
        clearAllData()
        loadData(defaultInitialPage)
    }

    override fun loadData(page: Int) {
        val postalCode: List<String> = arguments?.getStringArrayList(ARGUMENT_DATA_POSTAL_CODE)
                ?: arrayListOf()
        if (!TextUtils.isEmpty(searchInputView.searchText)) {
            filterList(postalCode, searchInputView.searchText).also {
                if (it.isNotEmpty()) {
                    convertToList(it)
                } else {
                    showNoResultMessage()
                }
            }
        } else {
            convertToList(postalCode)
        }
    }

    private fun convertToList(postalCode: List<String>) {
        val postalCodeViewModelList: MutableList<PostalCodeViewModel> = mutableListOf()
        for (result in postalCode) {
            val postalCodeViewModel = PostalCodeViewModel()
            postalCodeViewModel.postalCode = result
            postalCodeViewModelList.add(postalCodeViewModel)
        }
        renderList(postalCodeViewModelList)
        hideMessageSection()
    }

    private fun filterList(postalCode: List<String>, query: String): MutableList<String> {
        val result: MutableList<String> = mutableListOf()
        for (index in postalCode) {
            if (index.contains(query)) {
                result.add(index)
            }
        }
        return result
    }

    override fun onSearchTextChanged(text: String?) {
        clearAllData()
        loadData(defaultInitialPage)
    }

    override fun showNoResultMessage() {
        tv_message.text = getString(R.string.error_postal_code_empty)
        showMessageSection()
    }

    override fun showInitialLoadMessage() {
        tv_message.text = getString(R.string.message_advice_search_postal_code)
        showMessageSection()
    }

    override fun hideLoading() {
        hideMessageSection()
        super.hideLoading()
    }

    override fun showLoading() {
        hideMessageSection()
        super.showLoading()
    }

    private fun hideMessageSection() {
        tv_message.visibility = View.GONE
        swipe_refresh_layout.visibility = View.VISIBLE
    }

    private fun showMessageSection() {
        swipe_refresh_layout.visibility = View.GONE
        tv_message.visibility = View.VISIBLE
    }

}
