package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.ImageUploadPreviewActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.ImageUploadPreviewFragmentView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.ImageUploadFragmentPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.ImageUploadFragmentPresenterImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * Created by Nisie on 2/12/16.
 */
public class ImageUploadPreviewFragment extends
        BasePresenterFragment<ImageUploadFragmentPresenter>
        implements ImageUploadPreviewFragmentView {

    public static final String NAV_UPLOAD_IMAGE = "nav_upload_image";
    private static final int MAX_CHAR = 128;
    private static final String ARGS_IMAGE_LIST = "ARGS_IMAGE_LIST";
    private static final String ARGS_CAMERA_FILELOC = "ARGS_CAMERA_FILELOC";
    public static final int REQUEST_CODE_IMAGE_REVIEW = 532;
    public static final int MAX_IMAGE_LIMIT = 5;

    ViewPager previewImage;
    TextView submitButton;
    RecyclerView imageRecyclerView;
    EditText description;

    ImageUploadFragmentPresenter presenter;
    ImageUploadAdapter adapter;
    PreviewImageViewPagerAdapter viewPagerAdapter;
    int currentPosition = 0;

    public static Fragment createInstance(ArrayList<String> fileLoc, boolean isUpdate, int position) {
        ImageUploadPreviewFragment fragment = new ImageUploadPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageUploadHandler.FILELOC, fileLoc);
        bundle.putBoolean(ImageUploadPreviewActivity.IS_UPDATE, isUpdate);
        bundle.putInt(ImageUploadPreviewActivity.ARGS_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        adapter = ImageUploadAdapter.createAdapter(getActivity().getApplicationContext());
        if (savedInstanceState != null) {
            adapter.addList(savedInstanceState.<ImageUpload>getParcelableArrayList(ARGS_IMAGE_LIST));
            presenter.setCameraFileLoc(savedInstanceState.getString(ARGS_CAMERA_FILELOC,""));
        }
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getArguments() != null) {
            presenter.setImages(getArguments());
        }
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ImageUploadFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_image_upload_preview;
    }

    @Override
    protected void initView(View view) {

        previewImage = (ViewPager) view.findViewById(R.id.preview_image);
        submitButton = (TextView) view.findViewById(R.id.submit);
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.image_upload_list);
        description = (EditText) view.findViewById(R.id.image_description);

        adapter.setListener(onProductImageActionListener());
        adapter.setCanUpload(true);
        viewPagerAdapter = new PreviewImageViewPagerAdapter(adapter.getList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        imageRecyclerView.setAdapter(adapter);
        previewImage.setOffscreenPageLimit(0);
        previewImage.setAdapter(viewPagerAdapter);
        previewImage.addOnPageChangeListener(onPageChangeListener());
        description.addTextChangedListener(onTextChanged());

    }

    private TextWatcher onTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (description.getText().length() >= MAX_CHAR) {
                    description.setError(getString(R.string.max_caption_character));
                    description.requestFocus();
                } else {
                    description.setError(null);
                }
            }
        };
    }

    private ViewPager.OnPageChangeListener onPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPreviewImage(adapter.getList().get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private ImageUploadAdapter.ProductImageListener onProductImageActionListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getList().size() != 0) {
                            adapter.getList().get(currentPosition).setDescription(description.getText().toString());
                        }
                        openImagePicker();
                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, final ImageUpload image) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        adapter.getList().get(currentPosition).setDescription(description.getText().toString());
                        setPreviewImage(image);


                    }
                };
            }

        };
    }

    private void openImagePicker() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(com.tokopedia.tkpd.tkpdreputation.R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                null
                ,new ImagePickerMultipleSelectionBuilder(
                new ArrayList<>(),
                null,
                R.string.empty_desc,
                MAX_IMAGE_LIMIT - adapter.getList().size()));
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_REVIEW);
    }

    @Override
    protected void setViewListener() {
        submitButton.setOnClickListener(onSubmitImageUpload());
    }

    private View.OnClickListener onSubmitImageUpload() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.getList().isEmpty()) {
                    adapter.getList().get(currentPosition).setDescription(description.getText().toString());
                    presenter.onSubmitImageUpload(getAdapter().getList());
                } else {
                    getActivity().finish();
                }
            }
        };
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    public void setPreviewImage(final ImageUpload image) {
        viewPagerAdapter.notifyDataSetChanged();
        previewImage.setCurrentItem(image.getPosition());
        description.setText(image.getDescription());
        currentPosition = image.getPosition();
        setSelectedImageBorder(image.getPosition());

    }

    private void setSelectedImageBorder(int position) {
        for (int i = 0; i < adapter.getList().size(); i++) {
            if (i == position) {
                adapter.getList().get(i).setIsSelected(true);
            } else {
                adapter.getList().get(i).setIsSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public ImageUploadAdapter getAdapter() {
        return adapter;
    }

    private Drawable getDeleteMenu() {
        TextDrawable drawable = new TextDrawable(getActivity());
        drawable.setText(getResources().getString(R.string.action_delete));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.id.action_delete, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_delete);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getDeleteMenu());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            presenter.onDeleteImage(currentPosition);
        }
        return super.onOptionsItemSelected(item);
    }

    public PreviewImageViewPagerAdapter getPagerAdapter() {
        return viewPagerAdapter;

    }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    public void setDescription(String desc) {
        description.setText(desc);
    }

    public class PreviewImageViewPagerAdapter extends PagerAdapter {

        List<ImageUpload> list = new ArrayList<>();

        public PreviewImageViewPagerAdapter(List<ImageUpload> list) {
            this.list = list;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_slider, container, false);

            ImageView image = (ImageView) view
                    .findViewById(R.id.image);

            try {
                if (list.get(position).getFileLoc() == null) {
                    ImageHandler.LoadImage(image, list.get(position).getPicSrcLarge());
                } else {
                    ImageHandler.loadImageFromFile(getActivity(), image, new File(list.get(position).getFileLoc()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            container.addView(view);
            return view;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public List getList() {
            return this.list;
        }

        public void resetAdapter() {
            previewImage.setAdapter(null);
            previewImage.setAdapter(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARGS_IMAGE_LIST, adapter.getList());
        outState.putString(ARGS_CAMERA_FILELOC,presenter.getCameraFileLoc());
    }
}
