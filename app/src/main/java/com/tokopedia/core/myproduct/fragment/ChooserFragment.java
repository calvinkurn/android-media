package com.tokopedia.core.myproduct.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;
import com.tokopedia.core.myproduct.adapter.SimpleTextAdapter;
import com.tokopedia.core.myproduct.model.SimpleTextModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by m.normansyah on 12/9/15.
 */
public class ChooserFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String DATA_TO_CHOOSE = "DATA_TO_CHOOSE";//List<Pair<String, String>>
    private int mColumnCount = 1;
    private List<SimpleTextModel> simpelList;
    private OnListFragmentInteractionListener mListener;
    private SimpleTextAdapter simpleTextAdapter;
    RecyclerView recyclerView;


    public ChooserFragment() {
    }

    public static ChooserFragment newInstance(List<SimpleTextModel> data) {
        ChooserFragment fragment = new ChooserFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_TO_CHOOSE, Parcels.wrap(data));

        fragment.setArguments(args);
        return fragment;
    }

    public static ChooserFragment newInstance(int columnCount) {
        ChooserFragment fragment = new ChooserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT, 1);
            simpelList = Parcels.unwrap(getArguments().getParcelable(DATA_TO_CHOOSE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            simpleTextAdapter = new SimpleTextAdapter(simpelList, mListener);
            recyclerView.setAdapter(simpleTextAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(SimpleTextModel item);
        void onLongClick();
    }


}
