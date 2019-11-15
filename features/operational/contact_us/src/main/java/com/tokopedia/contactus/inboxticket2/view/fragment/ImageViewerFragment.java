package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.design.list.adapter.TouchImageAdapter;

import java.util.ArrayList;


public class ImageViewerFragment extends Fragment {
    private static final String IMAGE_ARRAY = "image_array";
    private static final String SCROLL_POSITION = "scroll_position";
    public static final String TAG = "ImageViewerFragment";
    private TouchViewPager vpImageViewer;
    private ImageView closeButton;

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
        View contentView = inflater.inflate(R.layout.contactus_layout_fragment_image_viewer, container, false);
        vpImageViewer = contentView.findViewById(R.id.vp_image_viewer);
        closeButton = contentView.findViewById(R.id.close_button);
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

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        return contentView;
    }

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
