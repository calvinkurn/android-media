package com.tokopedia.topads.edit.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.viewpager.TopAdsEditPagerAdapter
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.ADDED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.DELETED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.KATA_KUNCI
import com.tokopedia.topads.edit.utils.Constants.LAINNYA_NAME
import com.tokopedia.topads.edit.utils.Constants.PRODUK_NAME
import com.tokopedia.topads.edit.utils.Constants.TAB_POSITION
import com.tokopedia.topads.edit.view.fragment.edit.BaseEditKeywordFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditGroupAdFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditProductFragment
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_base_edit_activity_layout.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val CLICK_PRODUK_TAB = "click - tab produk"
private const val CLICK_KATA_KUNCI_TAB = "click - tab kata kunci"
private const val CLICK_ATUR_TAB = "click - tab atur"
private const val CLICK_SIMPAN_BUTTON = "click - simpan"

class EditFormAdActivity : BaseActivity(), HasComponent<TopAdsEditComponent>, SaveButtonStateCallBack {

    @Inject
    lateinit var viewModel: EditFormDefaultViewModel
    private lateinit var adapter: TopAdsEditPagerAdapter
    var list: ArrayList<Fragment> = ArrayList()
    var dataProduct = Bundle()
    var dataKeyword = HashMap<String, Any?>()
    var dataGroup = HashMap<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_base_edit_activity_layout)
        renderTabAndViewPager()
        setupToolBar()
        backArrow.setImageDrawable(AppCompatResources.getDrawable(this, com.tokopedia.topads.common.R.drawable.toolbar_back_black))
        backArrow.setOnClickListener {
            onBackPressed()
        }

        btn_submit.setOnClickListener {
            btn_submit?.isEnabled = false
            getDataFromChildFragments()
            saveChanges()
        }
    }

    private fun getDataFromChildFragments() {
        val fragments = (view_pager.adapter as TopAdsEditPagerAdapter).list
        for (fragment in fragments) {
            when (fragment) {
                is EditProductFragment ->
                    dataProduct = fragment.sendData()

                is EditGroupAdFragment ->
                    dataGroup = fragment.sendData()

                is BaseEditKeywordFragment -> {
                    dataKeyword = fragment.sendData()
                    sendSaveDataEvent(fragment.getKeywordNameItems())
                }
            }
        }
    }

    private fun saveChanges() {
        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup,
                ::onSuccessGroupEdited, ::onErrorEdit)
    }

    private fun sendSaveDataEvent(items: MutableList<Map<String, Any>>) {

        val map = mutableListOf<MutableMap<String, String>>()

        items.forEach {
            val keywordModel = DataLayer.mapOf(
                    Constants.KEYWORD_NAME, it["name"],
                    Constants.KEYWORD_ID, it["id"],
                    Constants.KEYWORD_TYPE, it["type"]
            )
            map.add(keywordModel as MutableMap<String, String>)
        }


        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormSaveEvent(CLICK_SIMPAN_BUTTON, map)
    }

    private fun onSuccessGroupEdited() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun onErrorEdit(error: String?) {
        val errorMessage = Utils.getErrorMessage(this, error ?: "")
        root?.let {
            Toaster.build(it, errorMessage,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)).show()
        }
        btn_submit?.isEnabled = true
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
    }

    private fun renderTabAndViewPager() {
        val bundle = intent.extras
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        tab_layout?.addNewTab(PRODUK_NAME)
        tab_layout?.addNewTab(KATA_KUNCI)
        tab_layout?.addNewTab(LAINNYA_NAME)
        tab_layout?.getUnifyTabLayout()?.getTabAt(bundle?.getInt(TAB_POSITION, 1) ?: 2)?.select()
        view_pager.currentItem = bundle?.getInt(TAB_POSITION, 1) ?: 2
        tab_layout?.getUnifyTabLayout()?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                when (p0.position) {
                    0 -> TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_PRODUK_TAB, "")
                    1 -> TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_KATA_KUNCI_TAB, "")
                    2 -> TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_ATUR_TAB, "")
                }
                view_pager.setCurrentItem(p0.position, true)
            }

        })
    }

    private fun getViewPagerAdapter(): TopAdsEditPagerAdapter {
        val bundle = intent.extras
        val list: ArrayList<Fragment> = ArrayList()
        list.add(EditProductFragment.newInstance(bundle))
        list.add(BaseEditKeywordFragment.newInstance(bundle))
        list.add(EditGroupAdFragment.newInstance(bundle))
        adapter = TopAdsEditPagerAdapter(arrayOf(PRODUK_NAME, KATA_KUNCI, LAINNYA_NAME), supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    override fun getComponent(): TopAdsEditComponent {
        val toAdsEditComponent = DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
        toAdsEditComponent.inject(this)
        return toAdsEditComponent
    }

    override fun onBackPressed() {
        if (checkChangesMade())
            showConfirmationDialog()
        else
            super.onBackPressed()
    }

    private fun checkChangesMade(): Boolean {
        getDataFromChildFragments()
        val dataAddProduct = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(ADDED_PRODUCTS)
        val dataDeleteProduct = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(DELETED_PRODUCTS)
        val keywordsPositiveCreate = dataKeyword[Constants.POSITIVE_CREATE] as? MutableList<*>
        val keywordsPositiveDelete = dataKeyword[Constants.POSITIVE_DELETE] as? MutableList<*>
        val keywordsNegCreate = dataKeyword[Constants.NEGATIVE_KEYWORDS_ADDED] as? MutableList<*>
        val keywordsNegDelete = dataKeyword[Constants.NEGATIVE_KEYWORDS_DELETED] as? MutableList<*>
        val keywordsPostiveEdit = dataKeyword[Constants.POSITIVE_EDIT] as? MutableList<*>
        val isDataChanged = dataGroup[Constants.IS_DATA_CHANGE] as? Boolean


        if (dataAddProduct?.isNotEmpty() != false || dataDeleteProduct?.isNotEmpty() != false || isDataChanged == true)
            return true
        if (keywordsPositiveCreate?.isNotEmpty() != false || keywordsPositiveDelete?.isNotEmpty() != false
                || keywordsNegCreate?.isNotEmpty() != false || keywordsNegDelete?.isNotEmpty() != false
                || keywordsPostiveEdit?.isNotEmpty() != false)
            return true

        return false

    }

    private fun showConfirmationDialog() {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.topads_edit_leave_page_conf_dialog_title))
        dialog.setDescription(getString(R.string.topads_edit_leave_page_conf_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.topads_common_save_butt))
        dialog.setSecondaryCTAText(getString(R.string.topads_edit_keluar))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            setButton(false)
            saveChanges()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            super.onBackPressed()
        }
        dialog.show()
    }

    private fun setButton(shouldEnable: Boolean) {
        btn_submit.isEnabled = shouldEnable
    }

    override fun setButtonState() {
        val fragments = (view_pager.adapter as TopAdsEditPagerAdapter).list
        var valid1 = true
        var valid2 = true
        var valid3 = true
        for (fragment in fragments) {
            when (fragment) {
                is EditProductFragment -> {
                    valid1 = fragment.getButtonState()
                }
                is EditGroupAdFragment -> {
                    valid2 = fragment.getButtonState()
                }
                is BaseEditKeywordFragment -> {
                    valid3 = fragment.getButtonState()
                }
            }
            btn_submit.isEnabled = valid1 && valid2 && valid3
        }
    }
}

interface SaveButtonStateCallBack {
    fun setButtonState()
}

