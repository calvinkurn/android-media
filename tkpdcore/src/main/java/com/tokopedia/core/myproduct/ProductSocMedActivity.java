package com.tokopedia.core.myproduct;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ArrayFragmentStatePagerAdapter;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.instoped.model.InstagramMediaModelParc;
import com.tokopedia.core.myproduct.dialog.DialogFragmentImageAddProduct;
import com.tokopedia.core.myproduct.fragment.ImageChooserDialog;
import com.tokopedia.core.myproduct.model.NoteDetailModel;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl;
import com.tokopedia.core.myproduct.service.ProductServiceConstant;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.myproduct.adapter.SmallPhotoAdapter;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.fragment.ChooserDialogFragment;
import com.tokopedia.core.myproduct.fragment.ChooserFragment;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.SimpleTextModel;
import com.tokopedia.core.myproduct.model.constant.ImageModelType;
import com.tokopedia.core.myproduct.presenter.ProductSocMedPresenter;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.myproduct.utils.AddProductType;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tokopedia.core.instoped.InstagramMediaModelUtil.convertTo;
import static com.tokopedia.core.myproduct.utils.VerificationUtils.validateAllInstoped;

/**
 * Created by m.normansyah on 4/7/16.
 */
public class ProductSocMedActivity extends BaseProductActivity implements ProductSocMedPresenter,
        ChooserFragment.OnListFragmentInteractionListener
        ,DownloadResultReceiver.Receiver, DownloadResultSender, DialogFragmentImageAddProduct.DFIAListener
        ,ImageChooserDialog.SelectWithImage
{
    public static final String DEFAULT_HTTP = "http://www.glamour.com/images/fashion/2016/03/Iskra-02-main.jpg";
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.products_soc_med_thumbnail)
    RecyclerView productsSocMedThumnNail;

    @BindView(R2.id.product_soc_med_viewpager)
    ViewPager productSocMedViewPager;

    PagerAdapter2 pagerAdapter;
    SmallPhotoAdapter adapter;

    DownloadResultReceiver mReceiver;
    ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
//                    recalculateView();

                    for(int i = 0; i< imageModels.size(); i++){
                        if(position==i)
                            continue;

                        ImageModel img = imageModels.get(i);
                        boolean isSelected = img.getTypes().contains(ImageModelType.SELECTED.getType());
                        img.clearAll();
                        if(isSelected){
                            img.setType(ImageModelType.SELECTED.getType());
                        }else{
                            img.setType(ImageModelType.UNSELECTED.getType());
                        }
                        img.setType(ImageModelType.INACTIVE.getType());
                        // set to list data
                        imageModels.set(i, img);
                    }

                    ImageModel imageModel = imageModels.get(position);
                    boolean isSelected = imageModel.getTypes().contains(ImageModelType.SELECTED.getType());
                    imageModel.clearAll();
                    if(isSelected){
                        imageModel.setType(ImageModelType.SELECTED.getType());
                    }else{
                        imageModel.setType(ImageModelType.UNSELECTED.getType());
                    }
                    imageModel.setType(ImageModelType.ACTIVE.getType());
                    imageModels.set(position, imageModel);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {


                }
            };

    SparseArray<InstagramMediaModel> instagramMediaModelSparseArray;

    private List<ImageModel> imageModels = new ArrayList<ImageModel>(){
        {
            ImageModel temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.UNSELECTED.getType());
            temp.setType(ImageModelType.INACTIVE.getType());
            add(temp);

            temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.UNSELECTED.getType());
            temp.setType(ImageModelType.ACTIVE.getType());
            add(temp);

            temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.SELECTED.getType());
            temp.setType(ImageModelType.INACTIVE.getType());
            add(temp);

            temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.SELECTED.getType());
            temp.setType(ImageModelType.ACTIVE.getType());
            add(temp);
        }
    };

    private List<InstagramMediaModel> images = new ArrayList<>();

    TkpdProgressDialog tkpdProgressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_socmed);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle(R.string.title_activity_add_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        if(intent != null ) {
            instagramMediaModelSparseArray
                    = Parcels.unwrap(intent.getParcelableExtra(PRODUCT_SOC_MED_DATA));

            //[START] convert instagram model to new models
            imageModels = new ArrayList<>();
            images.addAll(fromSparseArray(instagramMediaModelSparseArray));

            for (int i = 0; i < instagramMediaModelSparseArray.size(); i++) {
                String standardResolution = instagramMediaModelSparseArray.get(
                        instagramMediaModelSparseArray.keyAt(i)).standardResolution;
                ImageModel imageModel = new ImageModel(
                        standardResolution
                );
                imageModels.add(imageModel);
            }
        }
        //[END] convert instagram model to new models

        recreateAddProducts(images);

        //[START] set selection for first index
        ImageModel imageModel = imageModels.get(0);
        imageModel.clearAll();
        imageModel.setType(ImageModelType.UNSELECTED.getType());
        imageModel.setType(ImageModelType.ACTIVE.getType());
        imageModels.set(0, imageModel);
        //[START] set selection for first index

        adapter = new SmallPhotoAdapter(imageModels);
        adapter.setSmallPhotoAdapterTouch(new SmallPhotoAdapter.SmallPhotoAdapterTouch() {
            @Override
            public void movePosition(int position) {
                if(pagerAdapter != null && pagerAdapter.getCount() > 0 && position <= pagerAdapter.getCount()-1){
                    productSocMedViewPager.setCurrentItem(position);
                }
            }
        });
        productsSocMedThumnNail.setAdapter(adapter);
        productsSocMedThumnNail.setItemAnimator(new DefaultItemAnimator());
        productsSocMedThumnNail.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

         /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    private List<InstagramMediaModel> fromSparseArray(SparseArray<InstagramMediaModel> data){
        List<InstagramMediaModel> modelList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            InstagramMediaModel rawData = data.get(
                    data.keyAt(i));
            modelList.add(rawData);
        }
        return modelList;
    }


    private void recreateAddProducts(List<InstagramMediaModel> girlImages) {
        List<InstagramMediaModelParc> items = convertTo(girlImages);
        pagerAdapter = new PagerAdapter2(getSupportFragmentManager(), items);
        productSocMedViewPager.setAdapter(pagerAdapter);
//        productSocMedViewPager.setOffscreenPageLimit(2);
        productSocMedViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //[START] test for getting fragment inside viewpager
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getFragment(0).WarningDialog();
//            }
//        }, 3000);
        //[END] test for getting fragment inside viewpager
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null){
            int position = data.getIntExtra(ProductActivity.ADD_PRODUCT_IMAGE_LOCATION
                    , ProductActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(ProductActivity.IMAGE_URL);
            Log.d(TAG, messageTAG + imageUrl+" & "+position);
            AddProductFragment adf = getFragment(productSocMedViewPager.getCurrentItem());
            if(adf != null && CommonUtils.checkNotNull(imageUrl)){
                adf.addImageAfterSelect(imageUrl, position);
            }
            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryBrowser.IMAGE_URLS);
            if(adf != null && checkCollectionNotNull(imageUrls)){
                adf.addImageAfterSelect(imageUrls);
            }
        }
    }

    @Override
    public AddProductFragment getFragment(int position){
        return (AddProductFragment) pagerAdapter.findFragmentByPosition(position);
    }

    @Override
    public int getCurrentFragmentPosition() {
        return productSocMedViewPager.getCurrentItem();
    }

    @Override
    public void removeFragment(int position) {

        // [START] remove fragment and recreates fragment
        imageModels.remove(position);
        images.remove(position);

        adapter.notifyDataSetChanged();
        pagerAdapter.remove(position);
        if(productSocMedViewPager.getChildCount() == countUploaded()){
            finish();
        }
    }

    private int countUploaded() {
        int count = 0;
        for (ImageModel imageModel : imageModels){
            ArrayList<Integer> types = imageModel.getTypes();
            if (types.contains(ImageModelType.SELECTED.getType())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void noitfyCompleted(int position) {
        ImageModel imageModel = imageModels.get(position);
        imageModel.clearAll();
        imageModel.setType(ImageModelType.SELECTED.getType());
        imageModel.setType(ImageModelType.ACTIVE.getType());

        imageModels.set(position, imageModel);

        ImageGalleryImpl.Pair<Boolean, String> booleanStringPair = validateAllInstoped(this, imageModels);

        if(booleanStringPair.getModel1()){
            goToManageProduct();
        }

        adapter.notifyItemChanged(position);

        if(checkAllPictureUploadedAndMoveToNextProduct(position)){
            goToManageProduct();
        }

    }

    private void goToManageProduct() {
        Intent intent = new Intent(this, ManageProduct.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ManageProduct.SNACKBAR_CREATE,true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean checkAllPictureUploadedAndMoveToNextProduct(int position) {

        for(int i = position; i < imageModels.size(); i++){
            ArrayList<Integer> types = imageModels.get(i).getTypes();
            if (!types.contains(ImageModelType.SELECTED.getType())) {
                productSocMedViewPager.setCurrentItem(i);
                return false;
            }
        }

        for (int i = 0 ; i < position; i ++ ){
            ArrayList<Integer> types = imageModels.get(i).getTypes();
            if (!types.contains(ImageModelType.SELECTED.getType())) {
                productSocMedViewPager.setCurrentItem(i);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onListFragmentInteraction(SimpleTextModel item) {
        AddProductFragment adf = getFragment(productSocMedViewPager.getCurrentItem());
        Fragment dialog = getSupportFragmentManager()
                .findFragmentByTag(ChooserDialogFragment.FRAGMENT_TAG);
        switch (((ChooserDialogFragment)dialog).getType()){
            case ProductActivity.ADD_PRODUCT_CATEGORY:
                if(adf != null ){
                    adf.addCategoryAfterSelectV2(item);
                }
                break;
            case ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE:
                if(adf != null){
                    adf.addEtalaseAfterSelect(item);
                }
                break;
        }
        if(dialog!=null){
            ((ChooserDialogFragment)dialog).dismiss();
        }
    }

    @Override
    public void onLongClick() {
        Log.e(TAG, "onLongClick not implemented yet");
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(ProductService.TYPE, ProductService.INVALID_TYPE);
        Fragment fragment = null;
        switch(type){
            case ProductService.ADD_PRODUCT:
            case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
            case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
            case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                // default position is "0"
                int position = resultData.getInt(ProductService.PRODUCT_POSITION, 0);
                fragment = getFragment(position);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }

        //check if Fragment implement necessary interface
        if(fragment!=null && fragment instanceof BaseView && type != ProductService.INVALID_TYPE){
            switch (resultCode) {
                case ProductService.STATUS_RUNNING:
                    switch(type) {
                        case ProductService.ADD_PRODUCT:
                        case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:

                            if(resultData.getBoolean(ProductService.RETRY_FLAG, false)){
                                boolean retry = resultData.getBoolean(ProductService.RETRY_FLAG, false);
                                ((BaseView)fragment).ariseRetry(type, retry);
                            }else {
                                showProgress(false);
                                ((BaseView) fragment).setData(type, resultData);
                            }
                            break;
                    }
                    break;
                case ProductService.STATUS_FINISHED:
                    switch(type){
                        case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
                            NoteDetailModel.Detail returnableNoteContent = Parcels.unwrap(resultData.getParcelable(ProductService.RETURNABLE_NOTE_CONTENT));
                            ((AddProductFragment)fragment).setProductReturnable(true);
                            ((AddProductFragment)fragment).saveReturnPolicyDetail(returnableNoteContent);
                            ((AddProductFragment)fragment).dismissReturnableDialog();
                            break;
                        case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                            ((AddProductFragment)fragment).clearAvailibilityOfShopNote();
                            ((AddProductFragment)fragment).checkAvailibilityOfShopNote();
                            returnableNoteContent = Parcels.unwrap(resultData.getParcelable(ProductService.RETURNABLE_NOTE_CONTENT));
                            ((AddProductFragment)fragment).setProductReturnable(true);
                            ((AddProductFragment)fragment).saveReturnPolicyDetail(returnableNoteContent);
                            ((AddProductFragment)fragment).dismissReturnableDialog();
                            break;
                    }
                    break;
                case ProductService.STATUS_ERROR:
                    switch(type){
                        case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
                        case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                            switch(resultData.getInt(ProductService.NETWORK_ERROR_FLAG, ProductService.INVALID_NETWORK_ERROR_FLAG)){
                                case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                                    ((BaseView)fragment).onNetworkError(type, " BAD_REQUEST_NETWORK_ERROR !!!");
                                    break;
                                case NetworkConfig.INTERNAL_SERVER_ERROR:
                                    ((BaseView)fragment).onNetworkError(type, " INTERNAL_SERVER_ERROR !!!");
                                    break;
                                case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                                    ((BaseView)fragment).onNetworkError(type, " FORBIDDEN_NETWORK_ERROR !!!");
                                    break;
                                case ProductService.INVALID_NETWORK_ERROR_FLAG :
                                default :
                                    String messageError = resultData.getString(ProductService.MESSAGE_ERROR_FLAG, ProductService.INVALID_MESSAGE_ERROR);
                                    if(!messageError.equals(ProductService.INVALID_MESSAGE_ERROR)){
                                        ((BaseView)fragment).onMessageError(type, messageError);
                                    }
                            }
                    }

            }// end of status download service
        }
    }

    @Override
    public void sendDataToInternet(int type, Bundle data) {
        switch (type){
            case ProductService.ADD_PRODUCT:
            case ProductServiceConstant.ADD_PRODUCT_WITHOUT_IMAGE:
            case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
            case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                ProductService.startDownload(this, mReceiver, data, type);
                break;
            default :
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    @Override
    public void editImage(int action, int position, int fragmentPosition, boolean isPrimary) {
        Fragment fragment = getFragment(fragmentPosition);
        switch (action){
            case DialogFragmentImageAddProduct.DELETE_IMAGE:
                Log.d(TAG, "image delete : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT){
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>(){{add(getString(R.string.error_delete_primary_image));}});
                } else {
                    ((AddProductFragment) fragment).removeImageSelected(position);
                }
                break;
            case DialogFragmentImageAddProduct.CHANGE_IMAGE:
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT){
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>(){{add(getString(R.string.error_change_primary_image));}});
                } else {
                    int emptyPicture = 6 - ((AddProductFragment)fragment).countPicture();
                    Log.i(TAG, messageTAG + " max photo will get : " + emptyPicture);
                    ProductActivity.moveToImageGallery(this, position);
                }
                break;
            case DialogFragmentImageAddProduct.ADD_DESCRIPTION:
                ((AddProductFragment) fragment).showImageDescDialog(position);
                break;
            case DialogFragmentImageAddProduct.CHANGE_TO_PRIMARY:
                ((AddProductFragment) fragment).setSelectedImageAsPrimary(position);
                break;
            case DialogFragmentImageAddProduct.PRIMARY_IMAGE:
                Log.d(TAG, "image default : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT){
                    ((AddProductFragment) fragment).showImageDescDialog(position);
                } else {
                    ((AddProductFragment) fragment).setSelectedImageAsPrimary(position);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void itemSelected(int index) {
        Fragment fragment = getFragment(productSocMedViewPager.getCurrentItem());
        if(fragment instanceof AddProductFragment){
            ((AddProductFragment)fragment).setProductCatalog(index);
        }
    }

    protected static class PagerAdapter2 extends ArrayFragmentStatePagerAdapter<InstagramMediaModelParc> {

        public PagerAdapter2(FragmentManager fm, List<InstagramMediaModelParc> items) {
            super(fm, items);
        }

        @Override
        public Fragment getFragment(InstagramMediaModelParc item, int position) {
            return AddProductFragment.newInstance3(AddProductType.ADD_FROM_SOCIAL_MEDIA.getType()
                    , convertTo(item), position);
        }
    }

    public void showProgress(boolean isShow) {
        if(isShow){
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
            tkpdProgressDialog.showDialog();
        }else if(tkpdProgressDialog != null && tkpdProgressDialog.isProgress()){
            tkpdProgressDialog.dismiss();
        }
    }


    public void changePicture(int position, ImageModel imageModel){
        adapter.changePicture(position,imageModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.home) {
            Log.d(TAG, messageTAG + " R.id.home !!!");
            return true;
        } else if (i == android.R.id.home) {
            Log.d(TAG, messageTAG + " android.R.id.home !!!");
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
