package com.tokopedia.topads.keyword.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.core.network.retrofit.response.TextErrorObject
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.topads.R
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.view.fragment.TopAdsNewBaseStepperFragment
import com.tokopedia.topads.common.view.widget.TkpdProgressDialog
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.dashboard.utils.ViewUtils
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
import kotlinx.android.synthetic.main.top_ads_empty_layout.*
import java.util.ArrayList
import javax.inject.Inject

class TopAdsKeywordNewAddFragment : TopAdsNewBaseStepperFragment<TopAdsKeywordNewStepperModel>(),
        TopAdsKeywordAddAdapter.OnKeywordRemovedListener, TopAdsKeywordNewAddView{

    private var maxKeyword = MAX_KEYWORD
    private var serverCount = 0
    private var progressDialog: TkpdProgressDialog? = null
    private var onSuccessSaveKeywordListener: OnSuccessSaveKeywordListener? = null
    private var errorList: List<String> = arrayListOf()

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
        private const val EXTRA_ERROR_WORDS = "err_wrds"
        private const val EXTRA_LOCAL_WORDS = "lcl_wrds"

        fun newInstance(): Fragment = TopAdsKeywordNewAddFragment()
    }


    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: TopAdsKeywordNewStepperModel()
    }

    override fun saveStepperModel(stepperModel: TopAdsKeywordNewStepperModel) {}

    override fun gotoNextPage() {
        trackingSaveKeyword()
        showLoading()
        presenter.addKeywords(localKeywordAdapter.localKeywords)
    }

    override fun populateView(stepperModel: TopAdsKeywordNewStepperModel) {}

    override fun getScreenName(): String? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ads_keyword_new_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepperModel?.run {bidInfoTextView.visibility = if (isPositive) View.VISIBLE else View.GONE}
        buttonSave.setOnClickListener { gotoNextPage() }
        initEmptyStateView()
        checkButtonEnabled()
        setCurrentGroup()
        setCurrentMaxKeyword()
        setServerKeyword()
        setRecyclerView()
        needShowEmptyLayout()
    }

    private fun needShowEmptyLayout() {
        if (localKeywordAdapter.itemCount > 0){
            cardList.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        } else {
            cardList.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }
    }

    private fun initEmptyStateView() {
        activity?.let {
            no_result_image.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.icon_empty_local_keyword))
        }
        text_view_empty_title_text.setText(R.string.topads_keyword_local_empty_title)
        text_view_empty_content_text.visibility = View.GONE
        button_add_promo.visibility = View.GONE
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

    private fun setCurrentGroup(){
        keywordCurrentLabelView.run {
            title = getString(R.string.keyword_group_label_view,stepperModel?.groupName ?: "")
            setSubTitle(getString(R.string.top_ads_keyword_total_keyword_in_group_2, serverCount))
            if (serverCount > 0) {
                setOnClickListener { gotoKeywordCurrentList() }
            } else {
                setContent(null)
            }
        }
    }

    private fun gotoKeywordCurrentList() {
        stepperModel?.run {
            activity?.let {
                TopAdsKeywordCurrentListActivity.start(it, isPositive, groupId!!, this@TopAdsKeywordNewAddFragment)
            }
        }
    }

    private fun gotoAddKeywordItem(){
        activity?.let {
            TopAdsKeywordNewItemActivity.startForResult(it, REQUEST_ADD_ITEM_CODE,
                    localKeywordAdapter.localKeywords, maxKeyword, stepperModel, this)
        }
    }

    private fun setCurrentMaxKeyword(){
        keywordGroupLabelView.title = getString(R.string.top_ads_keywords_total_current_and_max,
                getLocalKeyWordSize(), maxKeyword)
        keywordGroupLabelView.setOnClickListener { gotoAddKeywordItem() }
    }

    private fun getLocalKeyWordSize() = localKeywordAdapter.itemCount

    private fun setServerKeyword(){}

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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ADD_ITEM_CODE){
            val localKeywords = data?.getParcelableArrayListExtra<AddKeywordDomainModelDatum>(TopAdsKeywordNewItemFragment.PARAM_ADDED_KEYWORDS)
            localKeywords?.run {
                localKeywordAdapter.addBulk(localKeywords)
                if (isAdded) {
                    setCurrentMaxKeyword()
                    checkButtonEnabled()
                    needShowEmptyLayout()
                }
            }
        }
    }

    fun clearKeywords() = localKeywordAdapter.clear()

    fun isButtonSaveEnabled() = buttonSave.isEnabled

    override fun onRemoved(position: Int) {
        setCurrentMaxKeyword()
        checkButtonEnabled()
        needShowEmptyLayout()
    }

    override fun onSuccessSaveKeyword() {
        hideLoading()
        Toast.makeText(activity, MethodChecker.fromHtml(getString(R.string.top_ads_keyword_has_been_added)), Toast.LENGTH_LONG).show()
        onSuccessSaveKeywordListener?.onSuccessSave(localKeywordAdapter.localKeywords)
        stepperListener?.finishPage()
    }

    override fun onFailedSaveKeyword(e: Throwable) {
        hideLoading()
        NetworkErrorHelper.showCloseSnackbar(activity,
                ViewUtils.getErrorMessage(activity, e))
        if (e is ResponseErrorException){
            errorList = convertResponseErrorToErrorList(e.errorList).map { it.toLowerCase() }
            localKeywordAdapter.settErrorList(errorList)
        }
    }

    private fun convertResponseErrorToErrorList(errorList: List<Error>?): List<String> {
        if (errorList == null){
            return arrayListOf()
        } else {
            val tempList = arrayListOf<String>()
            errorList.forEach {
                val tmp = it.getObjectParse(TextErrorObject::class.java).textList;
                if (tmp != null && tmp.size > 0){
                    tempList.addAll(tmp)
                }
            }
            return tempList
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.let { it.putParcelableArrayList(EXTRA_LOCAL_WORDS, ArrayList(localKeywordAdapter.localKeywords)) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        localKeywordAdapter.clear()
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_LOCAL_WORDS)) {
            localKeywordAdapter.addBulk(savedInstanceState.getParcelableArrayList(EXTRA_LOCAL_WORDS))
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_ERROR_WORDS)) {
            errorList = savedInstanceState.getStringArrayList(EXTRA_ERROR_WORDS)
        }
    }

    interface OnSuccessSaveKeywordListener{
        fun onSuccessSave(keywords: List<AddKeywordDomainModelDatum>)
    }
}