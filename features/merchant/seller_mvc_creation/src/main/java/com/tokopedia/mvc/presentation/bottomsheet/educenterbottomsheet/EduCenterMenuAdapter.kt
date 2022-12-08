package com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.model.EduCenterMenuModel
import com.tokopedia.mvc.R

class EduCenterMenuAdapter(val context: Context,
                           val listener: EduCenterClickListener) : RecyclerView.Adapter<EduCenterMenuViewHolder>()  {

    companion object{
        private const val url_main_article_about_coupon = "https://seller.tokopedia.com/edu/kupon-toko-saya/"
    }
    private var educationCenterMenuList = arrayListOf(
        EduCenterMenuModel(context.resources?.getString(R.string.smvc_edu_center_about_my_coupon)),
        EduCenterMenuModel(context.resources?.getString(R.string.smvc_edu_center_read_article), url_main_article_about_coupon)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EduCenterMenuViewHolder {
        return EduCenterMenuViewHolder(
            LayoutInflater.from(context).inflate(EduCenterMenuViewHolder.LAYOUT, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: EduCenterMenuViewHolder, position: Int) {
        holder.bind(educationCenterMenuList[position])
    }

    override fun getItemCount(): Int {
        return educationCenterMenuList.size
    }

}
