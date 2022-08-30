package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TmMemberListVh(itemView : View,private val context:Context) : RecyclerView.ViewHolder(itemView) {
    private var name : Typography
    private var image : ImageUnify

    init{
        name = itemView.findViewById(R.id.tm_member_list_item_name)
        image = itemView.findViewById(R.id.tm_member_list_item_image)
    }

    fun bind(memberName:String){
       name.text = memberName
        loadImage()
    }

    private fun loadImage(){
        Glide.with(context)
            .load(AVATAR_URL)
            .into(image)
    }

    companion object{
        private const val AVATAR_URL = "https://s3-alpha-sig.figma.com/img/cbf3/9bb1/cbcb65989d6c190137df523b9fdc8c6d?Expires=1662940800&Signature=e3Z7R8bzjzrbf~5Czvb1CjL~7VU8EMFstJyTcqytAlkdSOYi2GDBPrQA5Wl0cNWR70G7O6YOTJbD4P36HkSkMtxDGZFo-luVmJNjxAwPuXCeeQlPSiAF9kpb5GwWV9-h9S4k-VYzGh4-245CQyle7w6TYoXt~GA8d0Hid58hYDSeRRw4y0gzYW5p6dfDk0v-mTE00WlQEyTlWFjjEWNAWrB9xI3KsbZEHS~ZjRy2oHTLqMvAizNrXcoHiOanljttdd1D7obJtAi6TBd09pu3usSXTZGDtT0b1Kwvq8B0dv14zmCvmCUi4p8WLUvPchp1dOiou2mus9F~hoJ7669T6Q__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
    }
}