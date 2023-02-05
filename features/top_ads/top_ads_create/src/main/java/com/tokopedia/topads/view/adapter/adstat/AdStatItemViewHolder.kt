package com.tokopedia.topads.view.adapter.adstat

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.constants.MpTopadsConst.CONST_0
import com.tokopedia.topads.constants.MpTopadsConst.CONST_1
import com.tokopedia.topads.create.databinding.AdGroupStatItemBinding
import com.tokopedia.topads.view.datamodel.AdStatModel

class AdStatItemViewHolder(private val binding:AdGroupStatItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data:AdStatModel){
        if(data.loading){
            binding.root.setDisplayedChild(CONST_0)
        }
        else {
            binding.apply {
                root.setDisplayedChild(CONST_1)
                adStatTv.text = getCombinedStat(data)
            }
        }

    }

    private fun getCombinedStat(data: AdStatModel) : SpannableString{
        val stat = "${data.value} ${data.description}"
        val spannable = SpannableString(stat)
        val startIndex = stat.indexOf(data.description)
        val endIndex = startIndex + data.description.length
        if(startIndex<=endIndex){
            val descColor = ResourcesCompat.getColor(binding.root.context.resources,com.tokopedia.unifyprinciples.R.color.Unify_NN600,null)
            spannable.setSpan(
                ForegroundColorSpan(descColor),
                startIndex,
                endIndex,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }

    private fun FrameLayout.setDisplayedChild(index:Int) {
        for(idx in 0 until childCount){
            val view = getChildAt(idx)
            if(idx==index) {
                view.visibility = View.VISIBLE
            }
            else{
                view.visibility = View.GONE
            }
        }
    }
}
