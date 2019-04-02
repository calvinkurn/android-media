package com.tokopedia.challenges.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.adapter.TouchImageAdapter;

import java.util.ArrayList;



public class ImageViewerFragment extends Fragment implements View.OnClickListener {
    private static final String IMAGE_ARRAY = "image_array";
    private static final String SCROLL_POSITION = "scroll_position";
    public static final String TAG = "ImageViewerFragment";
    TouchViewPager vpImageViewer;
    ImageView closebtn;

    private TouchImageAdapter imageAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.layout_fragment_image_viewer, container, false);
        vpImageViewer = contentView.findViewById(R.id.vp_image_viewer);
        closebtn = contentView.findViewById(R.id.close_button);
        closebtn.setOnClickListener(this);
        imageAdapter = new TouchImageAdapter(getContext(), IMAGES_LOC);
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

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        vpImageViewer.setCurrentItem(SCROLL_POS);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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

    @Override
    public void onClick(View view) {
        getActivity().onBackPressed();
    }
}
