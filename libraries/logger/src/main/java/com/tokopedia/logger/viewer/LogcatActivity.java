package com.tokopedia.logger.viewer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.xtoast.XToast;
import com.tokopedia.logger.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogcatActivity extends AppCompatActivity
        implements TextWatcher, View.OnLongClickListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, LogcatManager.Listener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private final static String[] ARRAY_LOG_LEVEL = {"Verbose", "Debug", "Info", "Warn", "Error"};

    private final static File LOG_DIRECTORY = new File(Environment.getExternalStorageDirectory(), "Logcat");
    private final static String LOGCAT_TAG_FILTER_FILE = "logcat_tag_filter.txt";

    private List<LogcatInfo> mLogData = new ArrayList<>();

    private CheckBox mSwitchView;
    private View mSaveView;
    private TextView mLevelView;
    private EditText mSearchView;
    private View mEmptyView;
    private View mCleanView;
    private View mCloseView;
    private ListView mListView;
    private View mDownView;

    private LogcatAdapter mAdapter;

    private String mLogLevel = "V";

    private List<String> mTagFilter = new ArrayList<>();
    LogcatActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.logcat_window_logcat);
        model = new ViewModelProvider(this).get(LogcatActivityViewModel.class);

        mSwitchView = findViewById(R.id.iv_log_switch);
        mSaveView = findViewById(R.id.iv_log_save);
        mLevelView = findViewById(R.id.tv_log_level);
        mSearchView = findViewById(R.id.et_log_search);
        mEmptyView = findViewById(R.id.iv_log_empty);
        mCleanView = findViewById(R.id.iv_log_clean);
        mCloseView = findViewById(R.id.iv_log_close);
        mListView = findViewById(R.id.lv_log_list);
        mDownView = findViewById(R.id.ib_log_down);

        mAdapter = new LogcatAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mSwitchView.setOnCheckedChangeListener(this);
        mSearchView.addTextChangedListener(this);

        mSearchView.setText(LogcatConfig.getLogcatText());
        setLogLevel(LogcatConfig.getLogcatLevel());

        mSaveView.setOnClickListener(this);
        mLevelView.setOnClickListener(this);
        mEmptyView.setOnClickListener(this);
        mCleanView.setOnClickListener(this);
        mCloseView.setOnClickListener(this);
        mDownView.setOnClickListener(this);

        mSaveView.setOnLongClickListener(this);
        mSwitchView.setOnLongClickListener(this);
        mLevelView.setOnLongClickListener(this);
        mCleanView.setOnLongClickListener(this);
        mCloseView.setOnLongClickListener(this);

        LogcatManager.start(this);

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        }, 1000);

        if (!LOG_DIRECTORY.isDirectory()) {
            LOG_DIRECTORY.delete();
        }
        if (!LOG_DIRECTORY.exists()) {
            LOG_DIRECTORY.mkdirs();
        }
        initFilter();
        setViewModel();
    }

    private void setViewModel() {

        model.getLiveData().observe(this, logcatInfos -> {
            if (logcatInfos != null) {
                mAdapter.clearData();
                mAdapter.addItems(logcatInfos);

                mListView.setSelection(mAdapter.getCount() - 1);
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        });

        model.getSaveToFileLiveData().observe(this,text->{
            if(!TextUtils.isEmpty(text)){
                toast(text);
            }else {
                toast("Unable to save data");
            }
        });
    }

    @Override
    public void onReceiveLog(LogcatInfo info) {
        // Must not be in the filter list, and this log is printed by the current application
        if (Integer.parseInt(info.getPid()) == android.os.Process.myPid()) {
            if (!mTagFilter.contains(info.getTag())) {
                mListView.post(new LogRunnable(info));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.onItemClick(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new ChooseWindow(this)
                .setList("Copy Log", "Share Log", "Delete Log", "Block Log")
                .setListener(new ChooseWindow.OnListener() {
                    @Override
                    public void onSelected(final int location) {
                        switch (location) {
                            case 0:
                                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                if (manager != null) {
                                    manager.setPrimaryClip(ClipData.newPlainText("log", mAdapter.getItem(position).getLog()));
                                    toast("Log copied successfully");
                                } else {
                                    toast("Log copy failed");
                                }
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, mAdapter.getItem(position).getLog());
                                startActivity(Intent.createChooser(intent, "Share log"));
                                break;
                            case 2:
                                mLogData.remove(mAdapter.getItem(position));
                                mAdapter.removeItem(position);
                                break;
                            case 3:
                                addFilter(mAdapter.getItem(position).getTag());
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mSwitchView) {
            toast("Log capture switch");
        } else if (v == mSaveView) {
            toast("Save log");
        } else if (v == mLevelView) {
            toast("Log level filtering");
        } else if (v == mCleanView) {
            toast("Clear log");
        } else if (v == mCloseView) {
            toast("Close display");
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveView) {
            XXPermissions.with(this)
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(new OnPermission() {
                        @Override
                        public void hasPermission(List<String> granted, boolean all) {
                            if (all) {
                                saveLogToFile();
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean never) {
                            if (never) {
                                XXPermissions.startPermissionActivity(LogcatActivity.this, denied);
                                toast("Please grant storage permission before operating");
                            }
                        }
                    });
        } else if (v == mLevelView) {
            new ChooseWindow(this)
                    .setList(ARRAY_LOG_LEVEL)
                    .setListener(new ChooseWindow.OnListener() {
                        @Override
                        public void onSelected(int position) {
                            switch (position) {
                                case 0:
                                    setLogLevel("V");
                                    break;
                                case 1:
                                    setLogLevel("D");
                                    break;
                                case 2:
                                    setLogLevel("I");
                                    break;
                                case 3:
                                    setLogLevel("W");
                                    break;
                                case 4:
                                    setLogLevel("E");
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .show();
        } else if (v == mEmptyView) {
            mSearchView.setText("");
        } else if (v == mCleanView) {
            LogcatManager.clear();
            mAdapter.clearData();
        } else if (v == mCloseView) {
            onBackPressed();
        } else if (v == mDownView) {
            // 滚动到列表最底部
            mListView.smoothScrollToPosition(mAdapter.getCount() - 1);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            toast("Log capture is paused");
            LogcatManager.pause();
        } else {
            LogcatManager.resume();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String keyword = s.toString().trim();
        LogcatConfig.setLogcatText(keyword);
        mAdapter.setKeyword(keyword);
        mAdapter.clearData();
        model.searchText(mLogData, keyword);
    }

    private void setLogLevel(String level) {
        if (!level.equals(mLogLevel)) {
            mLogLevel = level;
            LogcatConfig.setLogcatLevel(level);
            afterTextChanged(mSearchView.getText());
            switch (level) {
                case "V":
                    mLevelView.setText(ARRAY_LOG_LEVEL[0]);
                    break;
                case "D":
                    mLevelView.setText(ARRAY_LOG_LEVEL[1]);
                    break;
                case "I":
                    mLevelView.setText(ARRAY_LOG_LEVEL[2]);
                    break;
                case "W":
                    mLevelView.setText(ARRAY_LOG_LEVEL[3]);
                    break;
                case "E":
                    mLevelView.setText(ARRAY_LOG_LEVEL[4]);
                    break;
                default:
                    break;
            }
        }
    }

    private class LogRunnable implements Runnable {

        private LogcatInfo info;

        private LogRunnable(LogcatInfo info) {
            this.info = info;
        }

        @Override
        public void run() {
            if (mLogData.size() > 0) {
                LogcatInfo lastInfo = mLogData.get(mLogData.size() - 1);
                if (info.getLevel().equals(lastInfo.getLevel()) &&
                        info.getTag().equals(lastInfo.getTag())) {

                    lastInfo.addLog(info.getLog());
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }

            mLogData.add(info);

            String content = mSearchView.getText().toString();
            if (TextUtils.isEmpty(content)) {
                mAdapter.addItem(info);
            } else {
                if (info.getLog().contains(content) || info.getTag().contains(content)) {
                    mAdapter.addItem(info);
                }
//                model.searchText(mLogData, content);
            }

//            if ("".equals(content) && "V".equals(mLogLevel)) {
//                mAdapter.addItem(info);
//            } else {
//                if (info.getLevel().equals(mLogLevel)) {
//                    if (info.getLog().contains(content) || info.getTag().contains(content)) {
//                        mAdapter.addItem(info);
//                    }
//                }
//            }
        }
    }

    private void initFilter() {
        File file = new File(LOG_DIRECTORY, LOGCAT_TAG_FILTER_FILE);
        if (file.exists() && file.isFile() && XXPermissions.hasPermission(this, Permission.MANAGE_EXTERNAL_STORAGE)) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? StandardCharsets.UTF_8 : Charset.forName("UTF-8")));
                String tag;
                while ((tag = reader.readLine()) != null) {
                    mTagFilter.add(tag);
                }
            } catch (IOException e) {
                toast("Failed to read shield configuration");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    private void addFilter(String tag) {
        mTagFilter.add(tag);
        BufferedWriter writer = null;
        try {
            File file = new File(LOG_DIRECTORY, LOGCAT_TAG_FILTER_FILE);
            if (!file.isFile()) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false),
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? StandardCharsets.UTF_8 : Charset.forName("UTF-8")));
            for (String temp : mTagFilter) {
                writer.write(temp + "\r\n");
            }
            writer.flush();

            ArrayList<LogcatInfo> removeData = new ArrayList<>();
            List<LogcatInfo> allData = mAdapter.getData();
            for (LogcatInfo info : allData) {
                if (info.getTag().equals(tag)) {
                    removeData.add(info);
                }
            }

            for (LogcatInfo info : removeData) {
                allData.remove(info);
                mAdapter.notifyDataSetChanged();
            }

            toast("Add block successfully：" + file.getPath());
        } catch (IOException e) {
            toast("Failed to add block");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void saveLogToFile() {
        model.saveToFile(mAdapter.getData(),getPackageName());
    }

    private void toast(CharSequence text) {
        new XToast(this)
                .setView(R.layout.logcat_window_toast)
                .setDuration(3000)
                .setGravity(Gravity.CENTER)
                .setAnimStyle(android.R.style.Animation_Toast)
                .setText(android.R.id.message, text)
                .show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogcatManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogcatManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogcatManager.destroy();
    }
}