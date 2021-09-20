package com.tokopedia.product.addedit.stub

import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import java.io.File
import java.io.FileOutputStream

class AddEditProductPreviewActivityStub: AddEditProductPreviewActivity() {

    override fun onStart() {
        super.onStart()
        val imagePath = generateFileImage().path

        getForegroundFragment()?.let {
            if (it is AddEditProductPreviewFragment) {
                val urlList = listOf(imagePath)

                it.viewModel.productInputModel.value?.detailInputModel?.pictureList = listOf()
                it.viewModel.updateProductPhotos(urlList, listOf())
            }
        }
    }

    private fun generateFileImage(): File {
        val file = File(externalCacheDir, "test.png")

        if (file.createNewFile()) {
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        }

        return file
    }

    private fun getForegroundFragment(): Fragment? {
        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(com.tokopedia.product.addedit.R.id.parent_view)
        return navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
    }
}
