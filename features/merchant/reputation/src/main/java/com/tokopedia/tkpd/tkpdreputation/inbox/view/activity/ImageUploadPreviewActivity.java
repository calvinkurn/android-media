package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.constant.Constant;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.ImageUploadPreviewFragment;

import java.util.ArrayList;

/**
 * @author by nisie on 9/4/17.
 */

public class ImageUploadPreviewActivity extends BaseSimpleActivity {

    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String ARGS_POSITION = "ARGS_POSITION";

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        ArrayList<String> fileLoc = new ArrayList<>();
        if (getIntent().getExtras() != null)
            fileLoc = getIntent().getExtras().getStringArrayList(Constant.ImageUpload.FILELOC);

        boolean isUpdate = false;
        if (getIntent().getExtras() != null)
            isUpdate = getIntent().getExtras().getBoolean(IS_UPDATE, false);

        int position = 0;
        if (getIntent().getExtras() != null)
            position = getIntent().getExtras().getInt(ARGS_POSITION, 0);

        return ImageUploadPreviewFragment.createInstance(fileLoc, isUpdate, position);
    }

    public static Intent getCallingIntent(Context context, ArrayList<String> fileLoc) {
        Intent intent = new Intent(context, ImageUploadPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.ImageUpload.FILELOC, fileLoc);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getUpdateCallingIntent(Context context, int position) {
        Intent intent = new Intent(context, ImageUploadPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_UPDATE, true);
        bundle.putInt(ARGS_POSITION, position);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.ImageUpload.REQUEST_CODE)
            getFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode,
                    resultCode, data);
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

}
