package com.tokopedia.profile.view.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.profile.R
import com.tokopedia.user.session.UserSession
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * @author by yfsx on 17/05/19.
 */
class ShareSocmedHandler(val activity: Activity) {
    private val PERMISSIONS = Arrays.asList("publish_actions")
    private lateinit var fbinterface: FacebookInterface

    interface FacebookInterface {
        fun onShareComplete()

        fun onShareFailed()
    }

    companion object {

        val EXCLUSION = "EXCLUSION"
    }

    /**
     * @param context
     * @param result  Hasil exclusion list dari web
     * @param uri     Alamat yang diterima dari intent
     * @return 0 Jika terdapat dalam exclude, GetShareIntent jika tidak terdapat dalam exclude
     */
//    fun GetNewExclusionList(context: Activity, result: JSONObject, uri: Uri): Int {
//        val link = uri.toString()
//        val temp: Int
//        try {
//            val data = JSONArray(result.getString("data"))
//            temp = data.length()
//            val ExclusionList = arrayOf<String>()
//            for (i in 0 until temp) {
//                ExclusionList[i] = data.getString(i)
//            }
//
//            val cache = LocalCacheHandler(context, EXCLUSION)
//            cache.putArrayString(TkpdCache.Key.URL, ExclusionList)
//            cache.applyEditor() // Update exclusion
//
//            for (string in ExclusionList) {
//                if (link.contains(string)) {
//                    val pm = context.packageManager
//                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tokopedia.com"))
//                    val af = myIntent.resolveActivityInfo(pm, 0)
//                    val launchIntent = Intent(Intent.ACTION_MAIN, Uri.parse(link))
//                    launchIntent.setClassName(af.packageName, af.name)
//                    context.startActivity(launchIntent)
//                    context.finish()
//                    return 0 // Terdapat dalam daftar exclude dari web, redirect ke browser
//                }
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        return GetShareIntent(context, uri) // Tidak terdapat exclusion apa2
//    }
//
//    fun SaveExclusionList(context: Activity, exclusionList: Array<String>) {
//
//    }


    /**
     * @param context
     * @param data    Alamat untuk di cek
     * @return 0 jika terdapat dalam exclusion dari cache, 1 masuk ke cek exclusion dari web
     */
    fun GetExclusionList(context: Activity, data: Uri): Int {
        val link = data.toString()
        val cache = LocalCacheHandler(context, EXCLUSION)
        val ExclusionList = cache.getArrayString(TkpdCache.Key.URL)
        // Mengambil daftar exlcude dari cache yang telah disimpan
        if (ExclusionList != null)
            for (string in ExclusionList!!) {
                if (link.contains(string)) {
                    val pm = context.packageManager
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                    val af = myIntent.resolveActivityInfo(pm, 0)
                    val launchIntent = Intent(Intent.ACTION_MAIN, Uri.parse(link))
                    launchIntent.setClassName(af.packageName, af.name)
                    context.startActivity(launchIntent)
                    context.finish()
                    return 0 // Terdapat dalam daftar exclude dari cache, redirect ke browser
                }
            }
        return 1 // Tidak terdapat dalam exclude, masuk ke fungsi cek exclusion dari web
    }

    /**
     * @param context
     * @param data    URI yang didapat dari intent
     * @return 1 Link valid, 2 Link tidak valid
     */
//    fun GetShareIntent(context: Activity, data: Uri): Int {
//        var link = data.toString()
//
//        // Untuk memproses URL jika berupa Hot, atau search atau link toko/produk
//        if (link.contains("/hot/")) {
//            link = link.replace("http://www.tokopedia.com/hot/", "")
//            link = link.replace("http://m.tokopedia.com/hot/", "")
//            link = link.replace("www.tokopedia.com/hot/", "")
//            link = link.replace("m.tokopedia.com/hot/", "")
//            context.startActivity(BrowseProductRouter.getBrowseProductIntent(context, link))
//            return 1 // 1 untuk pindah ke activity lain 2 untuk buka home
//        } else if (link.contains("/search?")) {
//            link = link.replace("http://www.tokopedia.com/search?", "")
//            link = link.replace("http://m.tokopedia.com/search?", "")
//            link = link.replace("www.tokopedia.com/hot/", "")
//            link = link.replace("m.tokopedia.com/hot/", "")
//            //TODO change to latest code
//            //            Intent intent = new Intent(context, BrowseCategory.class);
//            //            Bundle bundle = new Bundle();
//            //            bundle.putString("querry_data", link);
//            //            bundle.putInt("state", 4);
//            //            intent.putExtras(bundle);
//            //            context.startActivity(intent);
//            return 1
//        } else {
//            link = link.replace("http://www.tokopedia.com/", "")
//            link = link.replace("https://www.tokopedia.com/", "")
//            link = link.replace("http://m.tokopedia.com/", "")
//            link = link.replace("https://m.tokopedia.com/", "")
//            link = link.replace("www.tokopedia.com/hot/", "")
//            link = link.replace("m.tokopedia.com/hot/", "")
//            val div = link.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            if (div.size == 1) {
//                val intent = (context.application as TkpdCoreRouter).getShopPageIntentByDomain(context, div[0])
//                context.startActivity(intent)
//                context.startActivity(intent)
//                return 1
//            } else if (div.size == 2) {
//                val intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context)
//                val bundle = Bundle()
//                bundle.putString("shop_domain", div[0])
//                bundle.putString("product_key", div[1])
//                intent.putExtras(bundle)
//                context.startActivity(intent)
//                return 1
//            } else
//                return 2
//        }
//    }

    /**
     * Untuk Product, untuk share shop see ShareSpecificShop
     *
     * @param packageName nama package diambil di TkpdState.PackageName.*
     * @param targetType  jenis apakah gambar/textonly diambil di TkpdState.PackageName.Type_
     * @param altUrl      URL tempat share apabila apps tidak diinstal pada device, disimpan di TkpdState.PackageName.
     * @author EkaCipta
     */

    fun ShareSpecific(data: LinkerData, context: Activity, packageName: String, targetType: String, image: Bitmap?, altUrl: String) {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest<LinkerShareData>(0, DataMapper().getLinkerShareData(data), object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                        ShareData(context, packageName, targetType, linkerShareData.shareContents,
                                linkerShareData.shareUri, image, altUrl)
                    }

                    override fun onError(linkerError: LinkerError) {

                    }
                })
        )
    }

    fun ShareBranchUrl(context: Activity, packageName: String, targetType: String, branchUrl: String, shareContents: String) {
        ShareData(context, packageName, targetType, shareContents, branchUrl, null, null)

    }

    private fun ShareData(context: Activity?, packageName: String, targetType: String, shareTxt: String, ProductUri: String, image: Bitmap?, altUrl: String?) {
        var Resolved = false
        val share = Intent(Intent.ACTION_SEND)
        share.type = targetType
        var f: File? = null
        if (image != null)
            try {
                val bytes = ByteArrayOutputStream()
                CheckTempDirectory()
                f = File(Environment.getExternalStorageDirectory().toString() + File.separator + "tkpdtemp" + File.separator + uniqueCode() + ".jpg")
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        if (image != null) {
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, f))
        }
        share.putExtra(Intent.EXTRA_REFERRER, ProductUri)
        share.putExtra(Intent.EXTRA_TEXT, shareTxt)

        if (context != null) {
            if (context.packageManager != null) {
                val resInfo = context.packageManager.queryIntentActivities(share, 0)

                for (info in resInfo) {
                    if (info.activityInfo.packageName == packageName) {
                        Resolved = true
                        share.setPackage(info.activityInfo.packageName)
                    }
                }
            }

            if (Resolved) {
                context.startActivity(share)
            } else if (altUrl != null) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(altUrl)))
            } else
                Toast.makeText(context, context.getString(R.string.error_apps_not_installed), Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Untuk data uri (tidak perlu konversi dari bitmap ke uri)
     *
     * @param packageName nama package diambil di TkpdState.PackageName.*
     * @param targetType  jenis apakah gambar/textonly diambil di TkpdState.PackageName.Type_
     * @param altUrl      URL tempat share apabila apps tidak diinstal pada device, disimpan di TkpdState.PackageName.
     * @author EkaCipta
     */

    fun ShareSpecificUri(data: LinkerData, context: Activity, packageName: String, targetType: String, image: String, altUrl: String) {
        Observable.just(image)
                .map { image ->
                    var photo: File? = null
                    if (image != null) {
                        val future = Glide.with(context)
                                .load(image)
                                .downloadOnly(4096, 2160)
                        try {
                            photo = future.get()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                            throw RuntimeException(e.message)
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                            throw RuntimeException(e.message)
                        }

                    }
                    photo
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        object : Subscriber<File>() {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {

                            }

                            override fun onNext(file: File) {
                                LinkerManager.getInstance().executeShareRequest(
                                        LinkerUtils.createShareRequest<LinkerShareData>(0, DataMapper().getLinkerShareData(data),
                                                object : ShareCallback {
                                                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                                                        ShareDataWithSpecificUri(file, targetType, image, context,
                                                                linkerShareData.shareContents, linkerShareData.shareUri,
                                                                packageName, altUrl)
                                                    }

                                                    override fun onError(linkerError: LinkerError) {

                                                    }
                                                }
                                        )
                                )
                            }
                        }
                )

    }

    private fun ShareDataWithSpecificUri(file: File, targetType: String, image: String?, context: Activity?, shareTxt: String, ProductUri: String, packageName: String, altUrl: String?) {
        var Resolved = false
        val share = Intent(Intent.ACTION_SEND)
        share.type = targetType

        if (image != null) {
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, file))
        }
        share.putExtra(Intent.EXTRA_REFERRER, ProductUri)
        share.putExtra(Intent.EXTRA_HTML_TEXT, ProductUri)
        share.putExtra(Intent.EXTRA_TEXT, shareTxt)
        if (context != null) {
            if (context.packageManager != null) {
                val resInfo = context.packageManager.queryIntentActivities(share, 0)
                for (info in resInfo) {
                    if (info.activityInfo.packageName == packageName) {
                        Resolved = true
                        share.setPackage(info.activityInfo.packageName)
                    }
                }
            }
            if (Resolved) {
                context.startActivity(share)
            } else if (altUrl != null) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(altUrl)))
            } else
                Toast.makeText(context, context.getString(R.string.error_apps_not_installed), Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Cek direktori temporari untuk menyimpan gambar ada atau tidak
     */

    fun CheckTempDirectory() {
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "tkpdtemp" + File.separator
        val f = File(path)
        if (f.exists() && f.isDirectory) {
            Log.v("FILES", "EXIST")
            val fs = f.listFiles()
            if (fs != null && fs.size > 5)
            // Hapus jika jumlah gambar temporary > 5
                for (file in fs) {
                    file.delete()
                }
        } else {
            Log.v("FILES", "DONT EXIST")
            f.mkdir() // create directory jika direktori tidak ada
        }
    }

    fun uniqueCode(): String {
        val IDunique = UUID.randomUUID().toString()
        val id = IDunique.replace("-".toRegex(), "")
        return id.substring(0, 16)
    }

    fun ShareIntentImage(context: Activity, title: String, shareTxt: String,
                         ProductUri: String, icon: Bitmap?) {
        val bytes = ByteArrayOutputStream()
        CheckTempDirectory()
        val f = File(Environment.getExternalStorageDirectory().toString() + File.separator
                + "tkpdtemp" + File.separator + uniqueCode() + ".jpg")
        if (icon != null)
            try {
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        if (icon != null && f.exists()) {

            val uri = MethodChecker.getUri(context, f)
            val chooserIntent = createIntent(context, title, shareTxt, uri, true)
            context.startActivity(chooserIntent)
        } else {
            val chooserIntent = createIntent(context, title, shareTxt, null, false)
            context.startActivity(chooserIntent)
        }
    }

    private fun createIntent(context: Context, title: String, shareTxt: String, uri: Uri?,
                             fileExists: Boolean): Intent {
        val share = Intent(Intent.ACTION_SEND)
        val shareTitle: String
        if (fileExists) {
            share.type = "image/*"
            share.putExtra(Intent.EXTRA_STREAM, uri)
            share.putExtra(Intent.EXTRA_TEXT, shareTxt)
            shareTitle = "Share Image!"
        } else {
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_SUBJECT, title)
            share.putExtra(Intent.EXTRA_TEXT, shareTxt)
            shareTitle = "Share Link!"
        }


        val targetedShareIntents = ArrayList<Intent>()
        val resInfo = context.packageManager.queryIntentActivities(share, 0)

        var isShop = UserSession(activity).hasShop()

        if (!resInfo.isEmpty()) {
            for (info in resInfo) {
                val targetedShare = Intent(Intent.ACTION_SEND)
                if (fileExists) {
                    targetedShare.type = "image/*"
                    targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt)
                    targetedShare.putExtra(Intent.EXTRA_STREAM, uri)
                } else {
                    targetedShare.type = "text/plain"
                    targetedShare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    targetedShare.putExtra(Intent.EXTRA_SUBJECT, title)
                    targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt)
                }

                val appsName = info.activityInfo.packageName
                if (appsName.equals(context.packageName, ignoreCase = true)) {
                    if (isShop) {
                        targetedShare.setPackage(appsName)
                        targetedShareIntents.add(targetedShare)
                    }
                } else {
                    targetedShare.setPackage(appsName)
                    targetedShareIntents.add(targetedShare)
                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0),
                    shareTitle)
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toTypedArray<Parcelable>())
            return chooserIntent
        }
        val shareContent = Intent(Intent.ACTION_SEND)
        shareContent.putExtra(Intent.EXTRA_TEXT, shareTxt)
        return Intent.createChooser(shareContent,
                shareTitle)
    }

    fun ShareIntentImageUri(data: LinkerData, context: Activity, title: String?, imageUri: String) {

        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest<LinkerShareData>(0, DataMapper().getLinkerShareData(data), object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "text/plain"
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                        if (title != null) share.putExtra(Intent.EXTRA_SUBJECT, title)
                        share.putExtra(Intent.EXTRA_TEXT, linkerShareData.shareContents)
                        context.startActivity(Intent.createChooser(share, "Share link!"))
                    }

                    override fun onError(linkerError: LinkerError) {

                    }
                })
        )
    }

    /**
     * @param context
     * @param ProductUri bisa diganti url product
     * @param icon       kasih null untuk Shop
     * @author EkaCipta
     */

    fun ShareIntentImage(context: Activity, shareTxt: String, ProductUri: String, icon: Bitmap?) {
        val bytes = ByteArrayOutputStream()
        CheckTempDirectory()
        val f = File(Environment.getExternalStorageDirectory().toString() + File.separator + "tkpdtemp" + File.separator + uniqueCode() + ".jpg")
        if (icon != null)
            try {
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }


        //		Intent shareIntent = new Intent();
        //		 shareIntent.setAction(Intent.ACTION_SEND);
        //		 shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        //		 shareIntent.putExtra(Intent.EXTRA_TEXT, "Shareing Prodak nih: ");
        //		 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subjecct");
        //		 shareIntent.setType("image/jpeg");
        //		 shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //		 context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        try {
            val targetedShareIntents = ArrayList<Intent>()
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            val resInfo = context.packageManager.queryIntentActivities(share, PackageManager.MATCH_DEFAULT_ONLY)
            if (!resInfo.isEmpty()) {
                for (info in resInfo) {
                    try {
                        val targetedShare = Intent(Intent.ACTION_SEND)
                        //						  if (info.activityInfo.packageName.equals("com.google.android.gm") || info.activityInfo.packageName.equals("com.google.android.keep") || info.activityInfo.packageName.equals("com.google.android.apps.plus") || info.activityInfo.packageName.equals("com.google.android.talk")) {
                        targetedShare.type = "text/plain" // put here your mime type
                        //targetedShare.putExtra(Intent.EXTRA_SUBJECT,"Aplikasi Tokopedia");
                        //							  targetedShare.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.message_share_prod_info) + " " + ProductUri);
                        if (icon != null && info.activityInfo.packageName != "com.bbm") {
                            targetedShare.putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, f))
                            targetedShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        targetedShare.setPackage(info.activityInfo.packageName)
                        //						  else{
                        //							  targetedShare.setType("text/plain"); // put here your mime type
                        //							  targetedShare.setPackage(info.activityInfo.packageName);
                        //						  }
                        targetedShare.putExtra(Intent.EXTRA_REFERRER, ProductUri)
                        targetedShare.putExtra(Intent.EXTRA_HTML_TEXT, ProductUri)
                        targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt)
                        targetedShareIntents.add(targetedShare)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                //                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
                //                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                val chooserIntent = Intent.createChooser(share, "Select app to share")
                context.startActivity(chooserIntent)
            }
        } catch (e: Exception) {
            Log.v("VM", "Exception while sending image on " + e.message)
        }

    }

    fun ShareIntent(context: Activity, tagLine: String, ProductUri: String) {
        val targetedShareIntents = ArrayList<Intent>()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val resInfo = context.packageManager.queryIntentActivities(shareIntent, 0)
        if (!resInfo.isEmpty()) {
            for (resolveInfo in resInfo) {
                val targetedShareIntent = Intent(Intent.ACTION_SEND)
                targetedShareIntent.type = "text/plain"
                val packageName = resolveInfo.activityInfo.packageName
                //targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "App Tokopedia");
                // Untuk beda2in pada masing2 platform
                if (packageName == "com.facebook.katana") {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, ProductUri)
                } else {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "$tagLine $ProductUri")
                    //targetedShareIntents.add(targetedShareIntent);
                }
                targetedShareIntent.setPackage(packageName)
                targetedShareIntents.add(targetedShareIntent)
                //                if (packageName.equals("com.facebook.katana")){
                //                	targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just shared this product "+ProductUri);
                //                    targetedShareIntent.setPackage(packageName);
                //                    targetedShareIntents.add(targetedShareIntent);
                //                }
                //                else{
                //                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just shared this product "+ProductUri);
                //                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Select app to share")

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())

            context.startActivity(chooserIntent)
        }
    }
}