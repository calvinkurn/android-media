package com.tokopedia.tokofix.domain

import android.content.Context
import android.util.Log
import com.meituan.robust.PatchExecutor
import com.meituan.robust.RobustCallBack
import com.tokopedia.tokofix.domain.data.DataResponse
import com.tokopedia.tokofix.patch.PatchManipulatedImp
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import timber.log.Timber


/**
 * Author errysuprayogi on 09,February,2020
 */
class PatchRepository {

    private var TAG : String = PatchRepository::class.java.simpleName
    private var client: PatchApiService = RetrofitClient.webservice

    fun getPatch(ver: String, onSuccess: ((DataResponse) -> Unit)) {
        client.getPatch(ver).enqueue(object : Callback<DataResponse>{
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if(response.isSuccessful){
                    response.body()?.let(onSuccess)
                }
            }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun donwloadPatch(context: Context, url: String, robustCallBack: RobustCallBack) {
        client.downloadPatch(url).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    response.body()?.let{
                        val writtenToDisk: Boolean = writeResponseBodyToDisk(context, it)
                        if(writtenToDisk){
                            PatchExecutor(context, PatchManipulatedImp(robustCallBack), robustCallBack).start()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun writeResponseBodyToDisk(context: Context, body: ResponseBody): Boolean {
        return try {
            val file = File(context.getDir("patch",
                    Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "patch.jar")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream?.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream?.flush()
                Timber.w("P2#ROBUST#patch file download was success written to disk: " + file.absolutePath);
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }

}
