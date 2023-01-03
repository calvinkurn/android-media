package com.tokopedia.topads.edit.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.fastLazy
import com.tokopedia.topads.common.view.adapter.viewpager.TopAdsEditPagerAdapter
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.ADDED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.DELETED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.IS_BID_AUTOMATIC
import com.tokopedia.topads.edit.utils.Constants.KATA_KUNCI
import com.tokopedia.topads.edit.utils.Constants.LAINNYA_NAME
import com.tokopedia.topads.edit.utils.Constants.PRODUK_NAME
import com.tokopedia.topads.edit.utils.Constants.TAB_POSITION
import com.tokopedia.topads.edit.view.fragment.edit.BaseEditKeywordFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditGroupAdFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditProductFragment
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val CLICK_PRODUK_TAB = "click - tab produk"
private const val CLICK_KATA_KUNCI_TAB = "click - tab kata kunci"
private const val CLICK_ATUR_TAB = "click - tab atur"
private const val CLICK_SIMPAN_BUTTON = "click - simpan"
private const val CLICK_SIMPAN_BUTTON_EDIT = "click - simpan edit form"
private const val VIEW_EDIT_FORM = "view - edit form"
private const val CONST_0 = 0
private const val CONST_1 = 1
private const val CONST_2 = 2
private const val CONST_3 = 3

class EditFormAdActivity : BaseActivity(), HasComponent<TopAdsEditComponent>,
    SaveButtonStateCallBack {

    private var root: ConstraintLayout? = null
    private var toolbar: Toolbar? = null
    private var backArrow: ImageUnify? = null
    private var btnSubmit: Typography? = null
    private var tabLayout: TabsUnify? = null
    private var viewPager: TouchViewPager? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by fastLazy {
        ViewModelProvider(this, viewModelFactory)[EditFormDefaultViewModel::class.java]
    }
    private val groupId by fastLazy { intent.extras?.getString(Constants.GROUP_ID, "") ?: "" }

    private lateinit var adapter: TopAdsEditPagerAdapter
    var list: ArrayList<Fragment> = ArrayList()
    private var dataProduct = Bundle()
    private var dataKeyword = HashMap<String, Any?>()
    private var dataGroup = HashMap<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_base_edit_activity_layout)
        initInject()
        initView()
        loadGroupInfo()

        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendViewFormEvent(VIEW_EDIT_FORM, "")
        setupToolBar()
        backArrow?.setImageDrawable(AppCompatResources.getDrawable(this,
            com.tokopedia.topads.common.R.drawable.toolbar_back_black))
        backArrow?.setOnClickListener {
            onBackPressed()
        }

        btnSubmit?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(CLICK_SIMPAN_BUTTON_EDIT,
                "")
            btnSubmit?.isEnabled = false
            getDataFromChildFragments()
            saveChanges()
        }
    }

    private fun loadGroupInfo() {
        viewModel.getGroupInfo(groupId) {
            val isBidAutomatic = it.strategies.contains(ParamObject.AUTO_BID_STATE)
            renderTabAndViewPager(isBidAutomatic)
        }
    }

    private fun initView() {
        root = findViewById(R.id.root)
        toolbar = findViewById(R.id.toolbar)
        backArrow = findViewById(R.id.backArrow)
        btnSubmit = findViewById(R.id.btn_submit)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
    }

    private fun getDataFromChildFragments() {
        val fragments = (viewPager?.adapter as? TopAdsEditPagerAdapter)?.list
        if (fragments != null) {
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

        var isAutomatic = false
        dataKeyword[ParamObject.STRATEGIES]?.equals(ParamObject.AUTO_BID_STATE)
            ?.let {
                showToaster(it)
            }

        Handler(Looper.getMainLooper()).postDelayed({
            val returnIntent = Intent()
            returnIntent.putExtra(ParamObject.STRATEGIES, isAutomatic)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }, 1500)

    }

    private fun showToaster(automatic: Boolean) {
        val message = if(automatic) {
            getString(com.tokopedia.topads.common.R.string.bid_state_changed_automatic_successful)
        } else {
            getString(com.tokopedia.topads.common.R.string.bid_state_changed_manual_successful)
        }

        Toaster.build(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_NORMAL
        ).show()
    }

    private fun onErrorEdit(error: String?) {
        val errorMessage = Utils.getErrorMessage(this, error ?: "")
        root?.let {
            Toaster.build(it, errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)).show()
        }
        btnSubmit?.isEnabled = true
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
    }

    private fun renderTabAndViewPager(isBidAutomatic: Boolean) {
        val bundle = intent.extras
        viewPager?.adapter = getViewPagerAdapter(isBidAutomatic)
        viewPager?.offscreenPageLimit = CONST_3
        tabLayout?.addNewTab(PRODUK_NAME)
        tabLayout?.addNewTab(KATA_KUNCI)
        tabLayout?.addNewTab(LAINNYA_NAME)
        tabLayout?.getUnifyTabLayout()?.getTabAt(bundle?.getInt(TAB_POSITION, CONST_1) ?: CONST_2)?.select()
        viewPager?.currentItem = bundle?.getInt(TAB_POSITION, CONST_1) ?: CONST_2
        tabLayout?.getUnifyTabLayout()
            ?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    //do nothing
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    //do nothing
                }

                override fun onTabSelected(p0: TabLayout.Tab) {
                    when (p0.position) {
                        CONST_0 -> TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(
                            CLICK_PRODUK_TAB,
                            "")
                        CONST_1 -> TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(
                            CLICK_KATA_KUNCI_TAB,
                            "")
                        CONST_2 -> TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(
                            CLICK_ATUR_TAB,
                            "")
                    }
                    viewPager?.setCurrentItem(p0.position, true)
                }
            })

        viewPager?.let { tabLayout?.setupWithViewPager(it) }
    }

    private fun getViewPagerAdapter(isBidAutomatic: Boolean): TopAdsEditPagerAdapter {
        val bundle = Bundle(intent.extras) .apply {
            putBoolean(IS_BID_AUTOMATIC, isBidAutomatic)
        }

        val list: ArrayList<Fragment> = ArrayList()
        list.add(EditProductFragment.newInstance(bundle))
        list.add(BaseEditKeywordFragment.newInstance(bundle))
        list.add(EditGroupAdFragment.newInstance(bundle))
        adapter = TopAdsEditPagerAdapter(arrayOf(PRODUK_NAME, KATA_KUNCI, LAINNYA_NAME),
            supportFragmentManager,
            0)
        adapter.setData(list)
        return adapter
    }

    private fun initInject() {
        component.inject(this)
    }

    override fun getComponent(): TopAdsEditComponent {
        val toAdsEditComponent = DaggerTopAdsEditComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent)
            .topAdEditModule(TopAdEditModule(this)).build()
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
        val dataAddProduct =
            dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(
                ADDED_PRODUCTS)
        val dataDeleteProduct =
            dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(
                DELETED_PRODUCTS)
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
            || keywordsPostiveEdit?.isNotEmpty() != false
        )
            return true

        return false

    }

    private fun showConfirmationDialog() {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.dialogPrimaryCTA.isEnabled = btnSubmit?.isEnabled == true
        dialog.setTitle(getString(R.string.topads_edit_leave_page_conf_dialog_title))
        dialog.setDescription(getString(R.string.topads_edit_leave_page_conf_dialog_desc))
        dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_save_butt))
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
        btnSubmit?.isEnabled = shouldEnable
    }

    override fun setButtonState() {
        val fragments = (viewPager?.adapter as? TopAdsEditPagerAdapter)?.list
        var valid1 = true
        var valid2 = true
        var valid3 = true
        if (fragments != null) {
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
                btnSubmit?.isEnabled = valid1 && valid2 && valid3
            }
        }
    }
}

interface SaveButtonStateCallBack {
    fun setButtonState()
}

