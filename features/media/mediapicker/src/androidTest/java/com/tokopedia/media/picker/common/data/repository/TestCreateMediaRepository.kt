package com.tokopedia.media.picker.common.data.repository

import com.otaliastudios.cameraview.size.Size
import com.tokopedia.media.picker.data.repository.CreateMediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class TestCreateMediaRepository : CreateMediaRepository {
    override fun image(captureSize: Size?, byteArray: ByteArray?): Flow<File?> {
        return flow {  }
    }

    override fun video(): File? {
        return File.createTempFile("", "")
    }
}
