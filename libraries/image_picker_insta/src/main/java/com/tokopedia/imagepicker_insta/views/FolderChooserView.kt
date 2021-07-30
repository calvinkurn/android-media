package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R

class FolderChooserView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var rv:RecyclerView
    val dataList = arrayListOf<String>()
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
    }

    fun setData(list:List<String>){
        dataList.clear()
        dataList.addAll(list)
        adapter.notifyDataSetChanged()
    }
}