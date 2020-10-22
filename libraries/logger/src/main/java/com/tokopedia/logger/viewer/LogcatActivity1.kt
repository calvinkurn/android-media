package com.tokopedia.logger.viewer

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.View.OnLongClickListener
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.xtoast.XToast
import com.tokopedia.logger.R
import com.tokopedia.logger.viewer.LogcatActivity1
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class LogcatActivity1 : Activity(), TextWatcher, OnLongClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, LogcatManager.Listener, OnItemLongClickListener, AdapterView.OnItemClickListener {
    private val mLogData: MutableList<LogcatInfo> = ArrayList()
    private lateinit var mSwitchView: CheckBox
    private lateinit var mSaveView: View
    private lateinit var mLevelView: TextView
    private lateinit var mSearchView: EditText
    private lateinit var mEmptyView: View
    private lateinit var mCleanView: View
    private lateinit var mCloseView: View
    private lateinit var mListView: ListView
    private lateinit var mDownView: View
    private lateinit var mAdapter: LogcatAdapter
    private var mLogLevel = "V"

    /** Tag 过滤规则  */
    private val mTagFilter: MutableList<String?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.logcat_window_logcat)
        mSwitchView = findViewById(R.id.iv_log_switch)
        mSaveView = findViewById(R.id.iv_log_save)
        mLevelView = findViewById(R.id.tv_log_level)
        mSearchView = findViewById(R.id.et_log_search)
        mEmptyView = findViewById(R.id.iv_log_empty)
        mCleanView = findViewById(R.id.iv_log_clean)
        mCloseView = findViewById(R.id.iv_log_close)
        mListView = findViewById(R.id.lv_log_list)
        mDownView = findViewById(R.id.ib_log_down)
        mAdapter = LogcatAdapter()
        mListView.adapter = mAdapter
        mListView.onItemClickListener = this
        mListView.onItemLongClickListener = this
        mSwitchView.setOnCheckedChangeListener(this)
        mSearchView.addTextChangedListener(this)
        mSearchView.setText(LogcatConfig.getLogcatText())
        setLogLevel(LogcatConfig.getLogcatLevel())
        mSaveView.setOnClickListener(this)
        mLevelView.setOnClickListener(this)
        mEmptyView.setOnClickListener(this)
        mCleanView.setOnClickListener(this)
        mCloseView.setOnClickListener(this)
        mDownView.setOnClickListener(this)
        mSaveView.setOnLongClickListener(this)
        mSwitchView.setOnLongClickListener(this)
        mLevelView.setOnLongClickListener(this)
        mCleanView.setOnLongClickListener(this)
        mCloseView.setOnLongClickListener(this)

        // 开始捕获
        LogcatManager.start(this)
        mListView.postDelayed(Runnable { mListView.setSelection(mAdapter!!.count - 1) }, 1000)
        if (!LOG_DIRECTORY.isDirectory) {
            LOG_DIRECTORY.delete()
        }
        if (!LOG_DIRECTORY.exists()) {
            LOG_DIRECTORY.mkdirs()
        }
        initFilter()
    }

    override fun onReceiveLog(info: LogcatInfo) {
        // 这个 Tag 必须不在过滤列表中，并且这个日志是当前应用打印的
        if (info.pid!!.toInt() == Process.myPid()) {
            if (!mTagFilter.contains(info.tag)) {
                mListView!!.post(LogRunnable(info))
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        mAdapter!!.onItemClick(position)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View, position: Int, id: Long): Boolean {
        ChooseWindow(this)
                .setList("Copy Log", "Share Log", "Delete Log", "Block Log")
                .setListener { location ->
                    when (location) {
                        0 -> {
                            val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            if (manager != null) {
                                manager.setPrimaryClip(ClipData.newPlainText("log", mAdapter!!.getItem(position).log))
                                toast("Log copied successfully")
                            } else {
                                toast("Log copy failed")
                            }
                        }
                        1 -> {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT, mAdapter!!.getItem(position).log)
                            startActivity(Intent.createChooser(intent, "Share log"))
                        }
                        2 -> {
                            mLogData.remove(mAdapter!!.getItem(position))
                            mAdapter!!.removeItem(position)
                        }
                        3 -> addFilter(mAdapter!!.getItem(position).tag)
                        else -> {
                        }
                    }
                }
                .show()
        return true
    }

    override fun onLongClick(v: View): Boolean {
        if (v === mSwitchView) {
            toast("Log capture switch")
        } else if (v === mSaveView) {
            toast("Save log")
        } else if (v === mLevelView) {
            toast("Log level filtering")
        } else if (v === mCleanView) {
            toast("Clear log")
        } else if (v === mCloseView) {
            toast("Close display")
        }
        return true
    }

    override fun onClick(v: View) {
        if (v === mSaveView) {
            XXPermissions.with(this)
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(object : OnPermission {
                        override fun hasPermission(granted: List<String>, all: Boolean) {
                            if (all) {
                                saveLogToFile()
                            }
                        }

                        override fun noPermission(denied: List<String>, never: Boolean) {
                            if (never) {
                                XXPermissions.startPermissionActivity(this@LogcatActivity1, denied)
                                toast("请授予存储权限之后再操作")
                            }
                        }
                    })
        } else if (v === mLevelView) {
            ChooseWindow(this)
                    .setList(*ARRAY_LOG_LEVEL)
                    .setListener { position ->
                        when (position) {
                            0 -> setLogLevel("V")
                            1 -> setLogLevel("D")
                            2 -> setLogLevel("I")
                            3 -> setLogLevel("W")
                            4 -> setLogLevel("E")
                            else -> {
                            }
                        }
                    }
                    .show()
        } else if (v === mEmptyView) {
            mSearchView!!.setText("")
        } else if (v === mCleanView) {
            LogcatManager.clear()
            mAdapter!!.clearData()
        } else if (v === mCloseView) {
            onBackPressed()
        } else if (v === mDownView) {
            // 滚动到列表最底部
            mListView!!.smoothScrollToPosition(mAdapter!!.count - 1)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            toast("日志捕捉已暂停")
            LogcatManager.pause()
        } else {
            LogcatManager.resume()
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val keyword = s.toString().trim { it <= ' ' }
        LogcatConfig.setLogcatText(keyword)
        mAdapter!!.setKeyword(keyword)
        mAdapter!!.clearData()
        for (info in mLogData) {
            if ("V" == mLogLevel || info.level == mLogLevel) {
                if ("" != keyword) {
                    if (info.log!!.contains(keyword) || info.tag!!.contains(keyword)) {
                        mAdapter!!.addItem(info)
                    }
                } else {
                    mAdapter!!.addItem(info)
                }
            }
        }
        mListView!!.setSelection(mAdapter!!.count - 1)
        mEmptyView!!.visibility = if ("" == keyword) View.GONE else View.VISIBLE
    }

    private fun setLogLevel(level: String) {
        if (level != mLogLevel) {
            mLogLevel = level
            LogcatConfig.setLogcatLevel(level)
            afterTextChanged(mSearchView!!.text)
            when (level) {
                "V" -> mLevelView!!.text = ARRAY_LOG_LEVEL[0]
                "D" -> mLevelView!!.text = ARRAY_LOG_LEVEL[1]
                "I" -> mLevelView!!.text = ARRAY_LOG_LEVEL[2]
                "W" -> mLevelView!!.text = ARRAY_LOG_LEVEL[3]
                "E" -> mLevelView!!.text = ARRAY_LOG_LEVEL[4]
                else -> {
                }
            }
        }
    }

    private inner class LogRunnable(private val info: LogcatInfo) : Runnable {
        override fun run() {
            if (mLogData.size > 0) {
                val lastInfo = mLogData[mLogData.size - 1]
                if (info.level == lastInfo.level && info.tag == lastInfo.tag) {
                    lastInfo.addLog(info.log!!)
                    mAdapter!!.notifyDataSetChanged()
                    return
                }
            }
            mLogData.add(info)
            val content = mSearchView!!.text.toString()
            if ("" == content && "V" == mLogLevel) {
                mAdapter!!.addItem(info)
            } else {
                if (info.level == mLogLevel) {
                    if (info.log!!.contains(content) || info.tag!!.contains(content)) {
                        mAdapter!!.addItem(info)
                    }
                }
            }
        }

    }

    /**
     * 初始化 Tag 过滤器
     */
    private fun initFilter() {
        val file = File(LOG_DIRECTORY, LOGCAT_TAG_FILTER_FILE)
        if (file.exists() && file.isFile && XXPermissions.hasPermission(this, Permission.MANAGE_EXTERNAL_STORAGE)) {
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(InputStreamReader(FileInputStream(file),
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) StandardCharsets.UTF_8 else Charset.forName("UTF-8")))
                var tag: String?
                while (reader.readLine().also { tag = it } != null) {
                    mTagFilter.add(tag)
                }
            } catch (e: IOException) {
                toast("读取屏蔽配置失败")
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (ignored: IOException) {
                    }
                }
            }
        }
    }

    /**
     * 添加过滤的 TAG
     */
    private fun addFilter(tag: String?) {
        mTagFilter.add(tag)
        var writer: BufferedWriter? = null
        try {
            val file = File(LOG_DIRECTORY, LOGCAT_TAG_FILTER_FILE)
            if (!file.isFile) {
                file.delete()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file, false),
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) StandardCharsets.UTF_8 else Charset.forName("UTF-8")))
            for (temp in mTagFilter) {
                writer.write("""
    $temp

    """.trimIndent())
            }
            writer.flush()

            // 从列表中删除关于这个 Tag 的日志
            val removeData = ArrayList<LogcatInfo>()
            val allData = mAdapter!!.data
            for (info in allData) {
                if (info.tag == tag) {
                    removeData.add(info)
                }
            }
            for (info in removeData) {
                allData.remove(info)
                mAdapter!!.notifyDataSetChanged()
            }
            toast("添加屏蔽成功：" + file.path)
        } catch (e: IOException) {
            toast("添加屏蔽失败")
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }

    /**
     * 保存日志到本地
     */
    private fun saveLogToFile() {
        var writer: BufferedWriter? = null
        try {
            val directory = File(Environment.getExternalStorageDirectory(), "Logcat" + File.separator + packageName)
            if (!directory.isDirectory) {
                directory.delete()
            }
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, SimpleDateFormat("yyyyMMdd_kkmmss", Locale.getDefault()).format(Date()) + ".txt")
            if (!file.isFile) {
                file.delete()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file, false), Charset.forName("UTF-8")))
            val data = mAdapter!!.data
            for (info in data) {
                writer.write("""
    ${info.toString().replace("\n", "\r\n")}


    """.trimIndent())
            }
            writer.flush()
            toast("保存成功：" + file.path)
        } catch (e: IOException) {
            toast("保存失败")
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }

    /**
     * 吐司提示
     */
    private fun toast(text: CharSequence) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
//        XToast<>(this)
//                .setView(R.layout.logcat_window_toast)
//                .setDuration(3000)
//                .setGravity(Gravity.CENTER)
//                .setAnimStyle(android.R.style.Animation_Toast)
//                .setText(android.R.id.message, text)
//                .show()
    }

    override fun onBackPressed() {
        // 移动到上一个任务栈
        moveTaskToBack(false)
    }

    override fun onResume() {
        super.onResume()
        LogcatManager.resume()
    }

    override fun onPause() {
        super.onPause()
        LogcatManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogcatManager.destroy()
    }

    companion object {
        private val ARRAY_LOG_LEVEL = arrayOf("Verbose", "Debug", "Info", "Warn", "Error")
        private val LOG_DIRECTORY = File(Environment.getExternalStorageDirectory(), "Logcat")
        private const val LOGCAT_TAG_FILTER_FILE = "logcat_tag_filter.txt"
    }
}