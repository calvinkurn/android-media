package com.tokopedia.abstraction.base.view.webview;

import android.content.Intent;

@Deprecated
public interface FilePickerInterface {

     void startActivityForResult(Intent intent, int action);
}
