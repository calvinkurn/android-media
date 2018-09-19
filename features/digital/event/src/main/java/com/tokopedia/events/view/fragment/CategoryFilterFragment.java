package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.activity.EventFilterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ID;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class CategoryFilterFragment extends Fragment {


    public interface CategorySelectedListener {
        void setCategory(String selectedCategory);
    }


    Unbinder unbinder;
    private CategorySelectedListener listener;

    private static final String INDEX_CATEGORY = "index_category";
    @BindView(R2.id.rb_musik)
    RadioButton rbMusik;
    @BindView(R2.id.rb_seminar)
    RadioButton rbSeminar;
    @BindView(R2.id.rb_olahraga)
    RadioButton rbOlahraga;
    @BindView(R2.id.rb_teater)
    RadioButton rbTeater;
    @BindView(R2.id.rb_hiburan)
    RadioButton rbHiburan;
    @BindView(R2.id.rb_aktivitas)
    RadioButton rbAktivitas;
    @BindView(R2.id.rb_internasional)
    RadioButton rbInternasional;
    @BindView(R2.id.rb_opentrip)
    RadioButton rbOpentrip;

    private RadioButton currentToggled;

    private int mCategory;
    private String selectedCategoryId;


    public CategoryFilterFragment() {
        // Required empty public constructor
    }

    public static CategoryFilterFragment newInstance(int indexcategory) {
        CategoryFilterFragment fragment = new CategoryFilterFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX_CATEGORY, indexcategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt(INDEX_CATEGORY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View maincontent = inflater.inflate(R.layout.layout_fragment_filter_category, container, false);
        unbinder = ButterKnife.bind(this, maincontent);
        switch (mCategory) {
            case 0:
                rbHiburan.setChecked(true);
                currentToggled = rbHiburan;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 1:
                rbAktivitas.setChecked(true);
                currentToggled = rbAktivitas;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 2:
                rbMusik.setChecked(true);
                currentToggled = rbMusik;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 3:
                rbOlahraga.setChecked(true);
                currentToggled = rbOlahraga;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 4:
                rbTeater.setChecked(true);
                currentToggled = rbTeater;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 5:
                rbSeminar.setChecked(true);
                currentToggled = rbSeminar;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 6:
                rbInternasional.setChecked(true);
                currentToggled = rbInternasional;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 7:
                rbOpentrip.setChecked(true);
                currentToggled = rbOpentrip;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            default:

        }
        return maincontent;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EventFilterActivity) context;

    }

    @OnCheckedChanged({
            R2.id.rb_musik,
            R2.id.rb_seminar,
            R2.id.rb_olahraga,
            R2.id.rb_teater,
            R2.id.rb_hiburan,
            R2.id.rb_aktivitas,
            R2.id.rb_internasional,
            R2.id.rb_opentrip
    })
    public void onSelectCategory(CompoundButton button, boolean selected) {
        if (selected) {
            if (currentToggled != null)
                currentToggled.setChecked(false);
            currentToggled = (RadioButton) button;
            int id = button.getId();
            if (id == R.id.rb_hiburan)
                selectedCategoryId = CATEGORY_ID[0];
            else if (id == R.id.rb_aktivitas)
                selectedCategoryId = CATEGORY_ID[1];
            else if (id == R.id.rb_musik)
                selectedCategoryId = CATEGORY_ID[2];
            else if (id == R.id.rb_olahraga)
                selectedCategoryId = CATEGORY_ID[3];
            else if (id == R.id.rb_teater)
                selectedCategoryId = CATEGORY_ID[4];
            else if (id == R.id.rb_seminar)
                selectedCategoryId = CATEGORY_ID[5];
            else if (id == R.id.rb_internasional)
                selectedCategoryId = CATEGORY_ID[6];
            else if (id == R.id.rb_opentrip)
                selectedCategoryId = CATEGORY_ID[7];
        }
    }

    @OnClick(R2.id.tv_simpan)
    void onClickSimpan() {
        listener.setCategory(selectedCategoryId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
