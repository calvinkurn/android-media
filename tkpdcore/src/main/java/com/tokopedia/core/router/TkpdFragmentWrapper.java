package com.tokopedia.core.router;

import com.tokopedia.core.app.TkpdFragment;

/**
 * @author normansyahputa on 3/22/17.
 */
public class TkpdFragmentWrapper {
    String header;
    String TAG;
    TkpdFragment tkpdFragment;

    public TkpdFragmentWrapper(String header, String TAG, TkpdFragment tkpdFragment) {
        this.header = header;
        this.TAG = TAG;
        this.tkpdFragment = tkpdFragment;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public TkpdFragment getTkpdFragment() {
        return tkpdFragment;
    }

    public void setTkpdFragment(TkpdFragment tkpdFragment) {
        this.tkpdFragment = tkpdFragment;
    }
}
