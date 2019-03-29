package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.design.list.adapter.TouchImageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ImageViewerFragment extends Fragment {
    private static final String IMAGE_ARRAY = "image_array";
    private static final String SCROLL_POSITION = "scroll_position";
    public static final String TAG = "ImageViewerFragment";
    @BindView(R2.id.vp_image_viewer)
    TouchViewPager vpImageViewer;

    private ArrayList<String> IMAGES_LOC;
    private int SCROLL_POS;


    public ImageViewerFragment() {
        // Required empty public constructor
    }


    public static ImageViewerFragment newInstance(int postion, ArrayList<String> image_loc) {
        ImageViewerFragment fragment = new ImageViewerFragment();
        Bundle args = new Bundle();
        args.putInt(SCROLL_POSITION, postion);
        args.putStringArrayList(IMAGE_ARRAY, image_loc);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance");
        return fragment;
    }

    public void setImageData(int position, ArrayList<String> image_loc) {
        SCROLL_POS = position;
        IMAGES_LOC = image_loc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SCROLL_POS = getArguments().getInt(SCROLL_POSITION);
            IMAGES_LOC = getArguments().getStringArrayList(IMAGE_ARRAY);
        }
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.layout_fragment_image_viewer, container, false);
        ButterKnife.bind(this, contentView);
        TouchImageAdapter imageAdapter = new TouchImageAdapter(getContext(), IMAGES_LOC);
        vpImageViewer.setAdapter(imageAdapter);
        imageAdapter.SetonImageStateChangeListener(new TouchImageAdapter.OnImageStateChange() {

            @Override
            public void OnStateZoom() {
                vpImageViewer.setAllowPageSwitching(false);
            }

            @Override
            public void OnStateDefault() {
                vpImageViewer.setAllowPageSwitching(true);
            }
        });
        return contentView;
    }

    @OnClick(R2.id.close_button)
    void closeFragment() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
                actionBar.hide();
        }
        vpImageViewer.setCurrentItem(SCROLL_POS);
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickOpenImage,
                "");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
                actionBar.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
