package com.tokopedia.contactus.inboxticket2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.inboxticket2.data.model.BadCsatReasonListItem;
import com.tokopedia.contactus.inboxticket2.view.fragment.FragmentProvideRating;

import java.util.ArrayList;
import java.util.List;

public class ActivityProvideRating extends BaseSimpleActivity {
    public static final String CLICKED_EMOJI = "clicked_emoji";
    public static final String PARAM_COMMENT_ID = "comment_id";
    public static final String PARAM_OPTIONS_CSAT = "options_csat";
    public static Intent getInstance(Context context, int clickEmoji, String commentId, List<BadCsatReasonListItem> badCsatReasonListItems) {
        Intent i = new Intent(context,ActivityProvideRating.class);
        i.putExtra(CLICKED_EMOJI,clickEmoji);
        i.putExtra(PARAM_COMMENT_ID,commentId);
        i.putParcelableArrayListExtra(PARAM_OPTIONS_CSAT, (ArrayList<BadCsatReasonListItem>) badCsatReasonListItems);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return FragmentProvideRating.newInstance(getIntent().getExtras());
    }
}
