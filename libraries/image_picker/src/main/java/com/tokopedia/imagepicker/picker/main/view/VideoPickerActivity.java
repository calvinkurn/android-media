package com.tokopedia.imagepicker.picker.main.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.imagepicker.picker.main.builder.VideoPickerBuilder;

public class VideoPickerActivity extends ImagePickerActivity{

    public static Intent getIntent(Context context, VideoPickerBuilder videoPickerBuilder) {
        Intent intent = new Intent(context, VideoPickerActivity.class);
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, videoPickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        return intent;
    }

}
