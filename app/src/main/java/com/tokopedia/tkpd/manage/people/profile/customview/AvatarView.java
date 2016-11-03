package com.tokopedia.tkpd.manage.people.profile.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.manage.people.profile.model.Profile;
import com.tokopedia.tkpd.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;

import java.io.File;

import butterknife.Bind;

/**
 * Created on 6/9/16.
 */
public class AvatarView extends BaseView<Profile, ManagePeopleProfileFragmentPresenter> {

    private static final String TAG = AvatarView.class.getSimpleName();

    @Bind(R.id.loading)
    View loading;
    @Bind(R.id.avatar)
    ImageView avatar;


    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_manage_people_profile_avatar_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        loading.setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull Profile profile) {
        ImageHandler.LoadImage(avatar, profile.getDataUser().getUserImage());
        avatar.setOnClickListener(new AvatarClick());
    }

    @Override
    public void setPresenter(@NonNull ManagePeopleProfileFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadTemporaryPickedImage(String data) {
        File imgFile = new  File(data);
        ImageHandler.loadImageFromFile(getContext(), avatar, imgFile);
    }

    private class AvatarClick implements OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: #1" + presenter);
            Log.d(TAG, "onClick: #2" + getContext());
            presenter.setOnAvatarClickListener(getContext());
        }
    }
}
