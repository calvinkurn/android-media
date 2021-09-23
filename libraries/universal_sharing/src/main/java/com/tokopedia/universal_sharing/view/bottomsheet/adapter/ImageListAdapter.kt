package com.tokopedia.universal_sharing.view.bottomsheet.adapter


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ImageViewHolder

class ImageListAdapter(private val imageList: ArrayList<String>,
                       private val context: Context,
                       private val takeViewSS : (view: View, imageSaved: ((String)->Unit)) -> Unit,
                       private val imageSaved: (savedImagePath: String) -> Unit,
                       private val thumbNailUpdater: (thumbUrl: String) -> Unit) : RecyclerView.Adapter<ImageViewHolder>() {

    var selectedPosition : Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(View.inflate(context, ImageViewHolder.LAYOUT, null), this::updateSelectedPosition)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.selectedViewGroup.visibility = View.GONE
        holder.image.setImageUrl(imageList[position])
        if(selectedPosition == position) {
            holder.selectedViewGroup.visibility = View.VISIBLE
        }
        else{
            holder.selectedViewGroup.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateSelectedPosition(currentSelectedPosition : Int, view: View){
        selectedPosition = currentSelectedPosition
        takeViewSS(view, imageSaved)
        thumbNailUpdater(imageList[currentSelectedPosition])
        notifyDataSetChanged()
    }
}