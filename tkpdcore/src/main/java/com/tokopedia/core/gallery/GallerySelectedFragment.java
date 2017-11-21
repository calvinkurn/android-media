package com.tokopedia.core.gallery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;

import java.io.File;

public class GallerySelectedFragment extends Fragment implements AlbumMediaCollection.AlbumMediaCallbacks, AlbumMediaAdapter.OnMediaClickListener {

    private static final String ARG_PARAM_ALBUM = "ARG_PARAM_ALBUM";

    private AlbumItem albumItem;
    private RecyclerView recyclerview;
    private AlbumMediaAdapter adapter;
    private AlbumMediaCollection albumMediaCollection = new AlbumMediaCollection();

    public GallerySelectedFragment() {
        // Required empty public constructor
    }

    public static GallerySelectedFragment newInstance(AlbumItem albumItem) {
        GallerySelectedFragment fragment = new GallerySelectedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_ALBUM, albumItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_selected, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        albumItem = getArguments().getParcelable(ARG_PARAM_ALBUM);

        adapter = new AlbumMediaAdapter(getActivity(), recyclerview);
        adapter.registerOnMediaClickListener(this);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        int spacing = getResources().getDimensionPixelSize(R.dimen.media_grid_spacing);
        recyclerview.addItemDecoration(new MediaGridInset(3, spacing, false));
        recyclerview.setAdapter(adapter);
        albumMediaCollection.onCreate(getActivity(), this);
        albumMediaCollection.load(albumItem);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        albumMediaCollection.onDestroy();
    }

    @Override
    public void onAlbumMediaLoad(Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onAlbumMediaReset() {
        adapter.swapCursor(null);
    }

    @Override
    public void onMediaClick(AlbumItem album, MediaItem item, int adapterPosition) {
        // this finish here
        getHeightAndWidth(item);
        Intent intent = new Intent();
        intent.putExtra("EXTRA_RESULT_SELECTION", item);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void getHeightAndWidth(MediaItem item) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(item.getRealPath()).getAbsolutePath(), options);
        item.width = options.outWidth;
        item.height = options.outHeight;
    }

}
