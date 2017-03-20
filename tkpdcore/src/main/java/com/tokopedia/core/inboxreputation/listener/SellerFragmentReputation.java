package com.tokopedia.core.inboxreputation.listener;

import android.content.Context;

import com.tokopedia.core.app.TkpdFragment;

import java.util.List;

/**
 * Created by normansyahputa on 3/20/17.
 */

public interface SellerFragmentReputation {

    List<SellerReputationModel> getFragments(Context context);

    class SellerReputationModel {
        String header;
        String TAG;
        TkpdFragment tkpdFragment;

        public SellerReputationModel(String header, String TAG, TkpdFragment tkpdFragment) {
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
}
