package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.item_decoration.FolderItemDecoration
import com.tokopedia.imagepicker_insta.models.FolderData
import com.tokopedia.imagepicker_insta.views.adapters.FolderChooserAdapter
import com.tokopedia.unifycomponents.toPx

class FolderChooserView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var rv:RecyclerView
    val dataList = arrayListOf<FolderData>()
    val adapter = FolderChooserAdapter(dataList)

    fun getLayout() = R.layout.imagepicker_insta_folder_chooser_view
    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    fun initViews(){
        rv = findViewById(R.id.rv_folder_name)
        rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rv.adapter = adapter

        val itemDecoration = FolderItemDecoration(16.toPx())
        rv.addItemDecoration(itemDecoration)
    }

    fun itemOnClick(onClick:Function1<FolderData?,Unit>){
        adapter.onClick = onClick
    }

    fun setData(list:List<FolderData>){
        dataList.clear()
        dataList.addAll(list)
        adapter.notifyDataSetChanged()
    }
}