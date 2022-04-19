package com.tokopedia.salam.umrah.homepage.presentation.widget

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.model.UmrahHomepageDreamFundProgressWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_dream_fund_progress.view.*

/**
 * @author by M on 23/10/2019
 */
@Suppress("DEPRECATION")
class UmrahHomepageDreamFundProgressWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr){

    lateinit var umrahHomepageDreamFundProgressWidgetModel: UmrahHomepageDreamFundProgressWidgetModel

    init {
        View.inflate(context, R.layout.widget_umrah_dream_fund_progress, this)
    }

    fun buildView() {
        if (::umrahHomepageDreamFundProgressWidgetModel.isInitialized) {
            hideLoading()

            tg_widget_umrah_dream_fund_progress_reach_est.text = umrahHomepageDreamFundProgressWidgetModel.textReachEst
            pb_widget_umrah_dream_fund_progress.progress = umrahHomepageDreamFundProgressWidgetModel.progress
            tg_widget_umrah_dream_fund_progress_percentage.text = resources.getString(R.string.umrah_dream_fund_progress_percentage,umrahHomepageDreamFundProgressWidgetModel.progress)
            if(umrahHomepageDreamFundProgressWidgetModel.progress==MAX_PROGRESS){
                tg_widget_umrah_dream_fund_progress_percentage.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G500))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tg_widget_umrah_dream_fund_progress_percentage.background.setTint(resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G200))
                }
            }
            else if(umrahHomepageDreamFundProgressWidgetModel.progress==MIN_PROGRESS){
                tg_widget_umrah_dream_fund_progress_percentage.text = resources.getString(R.string.umrah_dream_fund_progress_not_started_yet)
                tg_widget_umrah_dream_fund_progress_percentage.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_44))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tg_widget_umrah_dream_fund_progress_target_progress.text = Html.fromHtml(umrahHomepageDreamFundProgressWidgetModel.textProgress, Html.FROM_HTML_MODE_COMPACT)
            } else {
                tg_widget_umrah_dream_fund_progress_target_progress.text = Html.fromHtml(umrahHomepageDreamFundProgressWidgetModel.textProgress)
            }
        } else {
            showLoading()
        }
    }

    private fun showLoading(){
        container_widget_umrah_dream_fund_progress.visibility = View.GONE
        container_widget_umrah_dream_fund_progress_shimmering.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        container_widget_umrah_dream_fund_progress.visibility = View.VISIBLE
        container_widget_umrah_dream_fund_progress_shimmering.visibility = View.GONE
    }

    companion object{
        const val MAX_PROGRESS = 100
        const val MIN_PROGRESS = 0
    }
}