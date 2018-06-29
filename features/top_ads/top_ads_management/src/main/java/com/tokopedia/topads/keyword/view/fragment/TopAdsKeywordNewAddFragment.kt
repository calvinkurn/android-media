package com.tokopedia.topads.keyword.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.seller.product.edit.utils.ViewUtils
import com.tokopedia.topads.R
import com.tokopedia.topads.common.view.fragment.TopAdsNewBaseStepperFragment
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordAddComponent
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordAddModule
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordCurrentListActivity
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewItemActivity
import com.tokopedia.topads.keyword.view.adapter.TopAdsKeywordAddAdapter
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewAddView
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordNewAddPresenter
import kotlinx.android.synthetic.main.fragment_top_ads_keyword_new_add.*
import javax.inject.Inject

class TopAdsKeywordNewAddFragment : TopAdsNewBaseStepperFragment<TopAdsKeywordNewStepperModel>(),
        TopAdsKeywordAddAdapter.OnKeywordRemovedListener, TopAdsKeywordNewAddView{

    private var maxKeyword = MAX_KEYWORD
    private var serverCount = 0
    private var progressDialog: TkpdProgressDialog? = null
    private var onSuccessSaveKeywordListener: OnSuccessSaveKeywordListener? = null

    @Inject lateinit var presenter: TopAdsKeywordNewAddPresenter

    private val localKeywordAdapter by lazy {
        TopAdsKeywordAddAdapter().apply {
            listener = this@TopAdsKeywordNewAddFragment
        }
    }

    companion object {
        val TAG = TopAdsKeywordNewAddFragment::class.java.simpleName
        const val REQUEST_ADD_ITEM_CODE = 1
        private const val MAX_KEYWORD = 50

        fun newInstance(): Fragment {
            return TopAdsKeywordNewAddFragment()
        }
    }


    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: TopAdsKeywordNewStepperModel()
    }

    override fun saveStepperModel(stepperModel: TopAdsKeywordNewStepperModel) {
    }

    override fun gotoNextPage() {
        trackingSaveKeyword()
        showLoading()
        presenter.addKeywords(localKeywordAdapter.localKeywords)
    }

    override fun populateView(stepperModel: TopAdsKeywordNewStepperModel) {}

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        DaggerTopAdsKeywordAddComponent.builder()
                .topAdsKeywordAddModule(TopAdsKeywordAddModule())
                .topAdsComponent(getComponent(TopAdsComponent::class.java))
                .build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateStepperModel()
        stepperModel?.run {
            maxKeyword = maxWords
            this@TopAdsKeywordNewAddFragment.serverCount = serverCount
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_top_ads_keyword_new_add, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSave.setOnClickListener { onButtonSaveClicked() }
        checkButtonEnabled()
        setCurrentGroup()
        setCurrentMaxKeyword()
        setServerKeyword()
        setRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnSuccessSaveKeywordListener){
            onSuccessSaveKeywordListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onSuccessSaveKeywordListener = null
    }

    private fun onButtonSaveClicked(){
        gotoNextPage()
    }

    private fun setCurrentGroup(){
        keywordCurrentLabelView.run {
            title = getString(R.string.keyword_group_label_view,stepperModel?.groupName ?: "")
            setSubTitle(getString(R.string.top_ads_keyword_total_keyword_in_group_2, serverCount))
            setOnClickListener { gotoKeywordCurrentList() }
        }
    }

    private fun gotoKeywordCurrentList() {
        stepperModel?.run {
            TopAdsKeywordCurrentListActivity.start(activity, isPositive, groupId!!,
                    this@TopAdsKeywordNewAddFragment)
        }
    }

    private fun gotoAddKeywordItem(){
        TopAdsKeywordNewItemActivity.startForResult(activity, REQUEST_ADD_ITEM_CODE,
                getLocalKeyWordSize(), maxKeyword, stepperModel, this)
    }

    private fun setCurrentMaxKeyword(){
        keywordGroupLabelView.title = getString(R.string.top_ads_keywords_total_current_and_max,
                getLocalKeyWordSize(), maxKeyword)
        keywordGroupLabelView.setOnClickListener { gotoAddKeywordItem() }
    }

    private fun getLocalKeyWordSize(): Int {
        return localKeywordAdapter.itemCount
    }

    private fun setServerKeyword(){

    }

    private fun checkButtonEnabled(){
        buttonSave.isEnabled = getLocalKeyWordSize() > 0
    }

    private fun setRecyclerView(){
        keywordRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        keywordRecyclerView.adapter = localKeywordAdapter
        keywordRecyclerView.addItemDecoration(DividerItemDecoration(activity))
    }

    /** need to be redefined from topads **/
    private fun trackingSaveKeyword() {

    }

    private fun showLoading(){
        progressDialog = progressDialog ?: TkpdProgressDialog(activity, TkpdProgressDialog.NORMAL_PROGRESS)
        progressDialog?.showDialog()
    }

    private fun hideLoading(){
        progressDialog?.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_ADD_ITEM_CODE){
                val localKeywords = data?.getParcelableArrayListExtra<AddKeywordDomainModelDatum>(TopAdsKeywordNewItemFragment
                        .ADDED_KEYWORDS_PARAM)
                localKeywords?.run {
                    localKeywordAdapter.addBulk(localKeywords)
                    setCurrentMaxKeyword()
                    checkButtonEnabled()
                }

            }
        }
    }

    fun clearKeywords(){
        localKeywordAdapter.clear()
    }

    fun isButtonSaveEnabled(): Boolean{
        return buttonSave.isEnabled
    }

    override fun onRemoved(position: Int) {
        setCurrentMaxKeyword()
        checkButtonEnabled()
    }

    override fun onSuccessSaveKeyword() {
        hideLoading()
        CommonUtils.UniversalToast(activity,
                getString(R.string.top_ads_keyword_has_been_added))
        onSuccessSaveKeywordListener?.onSuccessSave(localKeywordAdapter.localKeywords)
        stepperListener?.finishPage()
    }

    override fun onFailedSaveKeyword(e: Throwable) {
        hideLoading()
        NetworkErrorHelper.showCloseSnackbar(activity,
                ViewUtils.getErrorMessage(activity, e))
    }

    interface OnSuccessSaveKeywordListener{
        fun onSuccessSave(keywords: List<AddKeywordDomainModelDatum>)
    }
}