package com.tokopedia.imagepicker_insta.views

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.NoPermissionData
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.views.adapters.NoPermissionAdapter

class NoPermissionCameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    fun getLayout() = R.layout.imagepicker_insta_no_permission_camera_view
    lateinit var rv: RecyclerView
    val dataList = arrayListOf<NoPermissionData>()
    val adapter = NoPermissionAdapter(dataList)

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    fun initViews() {
        rv = findViewById(R.id.rv_cam)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        dataList.add(NoPermissionData(R.drawable.imagepicker_insta_ic_camera, context.getString(R.string.imagepicker_insta_akt_cam), {
            return@NoPermissionData PermissionUtil.hasArrayOfPermissions(context, arrayListOf(Manifest.permission.CAMERA))
        }, {
            return@NoPermissionData PermissionUtil.requestCameraAndWritePermission(context as AppCompatActivity)
        }
        ))
        dataList.add(NoPermissionData(R.drawable.imagepicker_insta_mic, context.getString(R.string.imagepicker_insta_akt_mic), {
            return@NoPermissionData PermissionUtil.hasArrayOfPermissions(context, arrayListOf(Manifest.permission.RECORD_AUDIO))
        }, {
            return@NoPermissionData PermissionUtil.requestMicPermission(context as AppCompatActivity)
        }))

        rv.adapter = adapter
    }


}