package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogLihatItemViewHolder

class CatalogLihatGridItemAdapter(
    private val childCategoryList: ArrayList<CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList>,
    private val catalogLibraryListener: CatalogLibraryListener
) : RecyclerView.Adapter<CatalogLihatItemViewHolder>() {

    var layoutInflater: LayoutInflater? = null

//    override fun getCount() = childCategoryList.size
//
//    override fun getItem(position: Int) = null
//
//    override fun getItemId(position: Int): Long = 0
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        var gridItemView = convertView
//        if (layoutInflater == null) {
//            layoutInflater =
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        }
//        if (gridItemView == null) {
//            gridItemView = layoutInflater?.inflate(R.layout.item_lihat_grid, null)
//        }
//        val childCategoryItem = childCategoryList[position]
//        childCategoryItem.categoryIconUrl?.let { iconUrl ->
//            gridItemView?.findViewById<ImageUnify>(R.id.lihat_item_icon)
//                ?.loadImageWithoutPlaceholder(iconUrl)
//
//        }
//        gridItemView?.findViewById<Typography>(R.id.lihat_item_title)?.text =
//            childCategoryItem.categoryName
//
//        return gridItemView
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLihatItemViewHolder {
        return CatalogLihatItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_lihat_grid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CatalogLihatItemViewHolder, position: Int) {
        holder.bind(childCategoryList[position], catalogLibraryListener)
    }

    override fun getItemCount() = childCategoryList.size

}
