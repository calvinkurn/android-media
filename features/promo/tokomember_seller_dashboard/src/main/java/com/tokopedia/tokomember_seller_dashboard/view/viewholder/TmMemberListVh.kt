package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.UserCardMemberModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TmMemberListVh(itemView : View,private val context:Context) : RecyclerView.ViewHolder(itemView) {
    private var name : Typography
    private var image : ImageUnify
    private var loader:AnimatedVectorDrawableCompat?

    init{
        name = itemView.findViewById(R.id.tm_member_list_item_name)
        image = itemView.findViewById(R.id.tm_member_list_item_image)
        loader = null
    }

    fun bind(data:UserCardMemberModel?){
       name.text = data?.member?.userInfo?.name.orEmpty()
        val url = data?.member?.userInfo?.profilePicture ?: AVATAR_URL
        loadImage(url)
    }

    private fun initLoader(){
        loader = loader ?: AnimatedVectorDrawableCompat.create(
            itemView.context,
            com.tokopedia.unifycomponents.R.drawable.unify_loader_shimmer
        )
        loader?.registerAnimationCallback(object :
            Animatable2Compat.AnimationCallback() {
            val mainHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                mainHandler.post {
                    loader?.start()
                }
            }
        })
        loader?.start()
    }

    private fun loadImage(url:String?){
        initLoader()
        Glide.with(context)
            .load(if(url.isNullOrEmpty()) AVATAR_URL else url)
            .placeholder(loader)
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    image.setImageDrawable(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    loader?.stop()
                }
            })
    }

    companion object{
        private const val AVATAR_URL = "https://s3-alpha-sig.figma.com/img/cbf3/9bb1/cbcb65989d6c190137df523b9fdc8c6d?Expires=1662940800&Signature=e3Z7R8bzjzrbf~5Czvb1CjL~7VU8EMFstJyTcqytAlkdSOYi2GDBPrQA5Wl0cNWR70G7O6YOTJbD4P36HkSkMtxDGZFo-luVmJNjxAwPuXCeeQlPSiAF9kpb5GwWV9-h9S4k-VYzGh4-245CQyle7w6TYoXt~GA8d0Hid58hYDSeRRw4y0gzYW5p6dfDk0v-mTE00WlQEyTlWFjjEWNAWrB9xI3KsbZEHS~ZjRy2oHTLqMvAizNrXcoHiOanljttdd1D7obJtAi6TBd09pu3usSXTZGDtT0b1Kwvq8B0dv14zmCvmCUi4p8WLUvPchp1dOiou2mus9F~hoJ7669T6Q__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        val LAYOUT_TYPE = R.layout.tm_member_list_item
    }
}

class TmMemberLoaderVh(itemView: View) : RecyclerView.ViewHolder(itemView){
    companion object{
        val LAYOUT_TYPE = R.layout.tm_member_list_infinite_loader
    }
}