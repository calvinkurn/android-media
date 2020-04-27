package com.tokopedia.topads.edit.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.param.GroupEditInput
import com.tokopedia.topads.edit.data.param.TopadsManageGroupAdsInput
import com.tokopedia.topads.edit.data.param.KeywordEditInput
import com.tokopedia.topads.edit.data.response.FinalAdResponse
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.adapter.TopAdsEditPagerAdapter
import com.tokopedia.topads.edit.view.fragment.edit.EditGroupAdFragment
import com.tokopedia.topads.edit.view.fragment.edit.BaseEditKeywordFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditProductFragment
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_base_edit_activity_layout.*
import java.util.HashMap
import javax.inject.Inject

class EditFormAdActivity : BaseActivity(), HasComponent<TopAdsEditComponent>, EditFormAdActivityCallback, SaveButtonStateCallBack {

    @Inject
    lateinit var viewModel: EditFormDefaultViewModel
    @Inject
    lateinit var userSession: UserSessionInterface
    private lateinit var adapter: TopAdsEditPagerAdapter
    var list: ArrayList<Fragment> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_base_edit_activity_layout)
        initInjector()
        renderTabAndViewPager()
        setupToolBar()
        backArrow.setOnClickListener {
            onBackPressed()
        }

        btn_submit.setOnClickListener {
            showConfirmationDialog(true)
        }
    }

    private fun initInjector() {
        DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun getDataFromChildFragments() {
        var variable: HashMap<String, Any> = HashMap()
        val fragments = (view_pager.adapter as TopAdsEditPagerAdapter).list
        var dataProduct = Bundle()
        var dataKeyword = HashMap<String, Any?>()
        var dataGroup = HashMap<String, Any?>()
        if (fragments[0] is EditProductFragment) {
            dataProduct = (fragments[0] as EditProductFragment).sendData()
        }
        if (fragments[1] is BaseEditKeywordFragment) {
            dataKeyword = (fragments[1] as BaseEditKeywordFragment).sendData()
        }
        if (fragments[2] is EditGroupAdFragment) {
            dataGroup = (fragments[2] as EditGroupAdFragment).sendData()
        }
        variable["input"] = convertToParam(dataProduct, dataKeyword, dataGroup, userSession)
        viewModel.topAdsCreated(variable, this::onSuccessUpdateAds, this::onErrorUpdateAds)
        finish()
    }

    private fun onSuccessUpdateAds(data: FinalAdResponse) {}

    private fun onErrorUpdateAds(e: Throwable) {}


    private fun setupToolBar() {
        setSupportActionBar(toolbar)
    }

    private fun renderTabAndViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        view_pager.currentItem = 2
        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getViewPagerAdapter(): TopAdsEditPagerAdapter {
        val bundle = intent.extras
        val list: ArrayList<Fragment> = ArrayList()
        list.add(EditProductFragment.newInstance(bundle))
        list.add(BaseEditKeywordFragment.newInstance(bundle))
        list.add(EditGroupAdFragment.newInstance(bundle))

        adapter = TopAdsEditPagerAdapter(supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackPressed() {
        showConfirmationDialog(false)
    }

    private fun showConfirmationDialog(dismiss: Boolean) {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.leave_page_conf_dialog_title))
        dialog.setDescription(getString(R.string.leave_page_conf_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.simpan))
        dialog.setSecondaryCTAText(getString(R.string.keluar))
        dialog.setPrimaryCTAClickListener {
            getDataFromChildFragments()
            //    dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            if (dismiss) {
                dialog.dismiss()
            } else {
                //finish()
                super.onBackPressed()
            }

        }
        dialog.show()
    }

    override fun onSaveData(data: String) {

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

fun convertToParam(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>, userSession: UserSessionInterface): TopadsManageGroupAdsInput {

    val NEGATIVE_KEYWORDS = "negative_keywords"
    val POSITIVE_CREATE = "createdPositiveKeyword"
    val POSITIVE_DELETE = "deletedPositiveKeyword"
    val POSITIVE_EDIT = "editedPositiveKeyword"
    val NEGATIVE_KEYWORDS_ADDED = "negative_keywords_added"
    val NEGATIVE_KEYWORDS_DELETED = "negative_keywords_deleted"
    val groupName: String = dataGroup["group_name"] as String
    val priceBidGroup = dataGroup["price_bid"] as? Int
    val dailyBudgetGroup = dataGroup["daily_budget"] as? Int
    val groupId = dataGroup["group_id"] as? Int
    val isNameEdited = dataGroup["isNameEdit"] as Boolean
    val dataAddProduct = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>("addedProducts")
    val dataDeleteProduct = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>("deletedProducts")


    val keywordsPositiveCreate = dataKeyword[POSITIVE_CREATE] as? MutableList<GetKeywordResponse.KeywordsItem>
    val keywordsPositiveDelete = dataKeyword[POSITIVE_DELETE] as? MutableList<GetKeywordResponse.KeywordsItem>
    val keywordsNegCreate = dataKeyword[NEGATIVE_KEYWORDS_ADDED] as? MutableList<GetKeywordResponse.KeywordsItem>
    val keywordsNegDelete = dataKeyword[NEGATIVE_KEYWORDS_DELETED] as? MutableList<GetKeywordResponse.KeywordsItem>
    val keywordsPostiveEdit = dataKeyword[POSITIVE_EDIT] as? MutableList<GetKeywordResponse.KeywordsItem>
    //   val keywordsNegative: MutableList<GetKeywordResponse.KeywordsItem>
    //   keywordsNegative = dataKeyword[NEGATIVE_KEYWORDS] as MutableList<GetKeywordResponse.KeywordsItem>

    //always
    val input = TopadsManageGroupAdsInput()
    input.groupID = groupId.toString()
    input.shopID = userSession.shopId
    input.source = "dashboard_edit_group"
    val groupInput = input.groupInput
    groupInput.action = "edit"
    val group = groupInput.group

    // if only group name is edited
    if (isNameEdited) {
        group?.type = "product"
        group?.name = groupName
    } else {
        group?.name = null
    }
    group?.type = "product"
    group?.status = "published"
    group?.scheduleStart = ""
    group?.scheduleEnd = ""
    group?.dailyBudget = dailyBudgetGroup
    group?.priceBid = priceBidGroup


    val productList: MutableList<GroupEditInput.Group.AdOperationsItem> = mutableListOf()
    val keywordList: MutableList<KeywordEditInput> = mutableListOf()



    dataAddProduct?.forEach { x ->
        val adOperation = GroupEditInput.Group.AdOperationsItem()
        val ad = GroupEditInput.Group.AdOperationsItem.Ad()
        ad.productId = x.itemID.toString()
        adOperation.ad = ad
        adOperation.action = "add"
        productList.add(adOperation)
    }
    dataDeleteProduct?.forEach { x ->
        val adOperation = GroupEditInput.Group.AdOperationsItem()
        val ad = GroupEditInput.Group.AdOperationsItem.Ad()
        ad.productId = x.itemID.toString()
        adOperation.ad = ad
        adOperation.action = "remove"
        productList.add(adOperation)
    }
    keywordsPositiveCreate?.forEach { x ->
        val keywordEditInput = KeywordEditInput()
        val keyword = KeywordEditInput.Keyword()
        keyword.source = "es"
        keyword.id = x.keywordId
        keyword.price_bid = x.priceBid
        keyword.status = "active"
        keyword.tag = x.tag
        if (x.type == 11) {
            keyword.type = "positive_phrase"
        } else {
            keyword.type = "positive_specific"
        }
        //  keyword.type = x.typeKeyword
        keywordEditInput.keyword = keyword
        keywordEditInput.action = "create"
        keywordList.add(keywordEditInput)
    }
    keywordsPostiveEdit?.forEach { x ->
        val keywordEditInput = KeywordEditInput()
        val keyword = KeywordEditInput.Keyword()
        keyword.price_bid = x.priceBid
        keyword.id = x.keywordId
        keyword.status = "active"
        keyword.tag = null
        keyword.type = null
        keyword.source = null
        keywordEditInput.keyword = keyword
        keywordEditInput.action = "edit"
        keywordList.add(keywordEditInput)

    }
    keywordsPositiveDelete?.forEach { x ->
        val keywordEditInput = KeywordEditInput()
        val keyword = KeywordEditInput.Keyword()
        keyword.price_bid = null
        keyword.id = x.keywordId
        keyword.tag = null
        keyword.status = null
        keyword.type = null
        keyword.source = null
        keywordEditInput.keyword = keyword
        keywordEditInput.action = "delete"
        keywordList.add(keywordEditInput)
    }

    keywordsNegCreate?.forEach { x ->
        val keywordEditInput = KeywordEditInput()
        val keyword = KeywordEditInput.Keyword()
        keyword.id = "0"
        keyword.price_bid = 0
        if (x.type == 12) {
            keyword.type = "negative_phrase"
        } else {
            keyword.type = "negative_specific"
        }
        keyword.status = "active"
        keyword.tag = x.tag
        keyword.source = "es"
        keywordEditInput.keyword = keyword
        keywordEditInput.action = "create"
        keywordList.add(keywordEditInput)
    }

    keywordsNegDelete?.forEach { x ->
        val keywordEditInput = KeywordEditInput()
        val keyword = KeywordEditInput.Keyword()
        keyword.price_bid = 0
        keyword.id = x.keywordId
        keyword.tag = null
        keyword.status = null
        keyword.type = null
        keyword.source = null
        keywordEditInput.keyword = keyword
        keywordEditInput.action = "delete"
        keywordList.add(keywordEditInput)
    }


    input.groupInput = groupInput
    input.groupInput.group = group
    if (productList.isNullOrEmpty())
        input.groupInput.group?.adOperations = null
    else
        input.groupInput.group?.adOperations = productList
    if (keywordList.isNullOrEmpty())
        input.keywordOperation = null
    else
        input.keywordOperation = keywordList
    return input
}


interface EditFormAdActivityCallback {
    fun onSaveData(text: String)
}

interface SaveButtonStateCallBack {
    fun setButtonState()
}

