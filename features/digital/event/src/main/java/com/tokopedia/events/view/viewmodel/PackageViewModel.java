
package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.events.data.entity.response.Form;

import java.util.List;

public class PackageViewModel implements Parcelable {

    private int id;
    private int productId;
    private int productScheduleId;
    private int productGroupId;
    private String providerScheduleId;
    private String providerTicketId;
    private String thumbnailApp;
    private String displayName;
    private String title;
    private String description;
    private String tnc;
    private int convenienceFee;
    private int mrp;
    private int commission;
    private String commissionType;
    private int salesPrice;
    private int sold;
    private int booked;
    private int available;
    private int minQty;
    private int maxQty;
    private String providerStatus;
    private int selectedQuantity;
    private String timeRange;
    private String address;
    private Form form;
    private String fetchSectionUrl;
    private List<Form> forms;
    private int digitalCategoryID;
    private int digitalProductID;
    private String digitalProductCode;
    private long startDate;
    private long endDate;
    private boolean isSaleStarted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private int categoryId;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductScheduleId() {
        return productScheduleId;
    }

    public void setProductScheduleId(int productScheduleId) {
        this.productScheduleId = productScheduleId;
    }

    public int getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getProviderScheduleId() {
        return providerScheduleId;
    }

    public void setProviderScheduleId(String providerScheduleId) {
        this.providerScheduleId = providerScheduleId;
    }

    public String getProviderTicketId() {
        return providerTicketId;
    }

    public void setProviderTicketId(String providerTicketId) {
        this.providerTicketId = providerTicketId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean getSaleStarted() {
        return isSaleStarted;
    }

    public void setSaleStarted(boolean saleStarted) {
        isSaleStarted = saleStarted;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public int getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(int convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }


    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }


    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getThumbnailApp() {
        return thumbnailApp;
    }

    public void setThumbnailApp(String thumbnailApp) {
        this.thumbnailApp = thumbnailApp;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFetchSectionUrl() {
        return fetchSectionUrl;
    }

    public void setFetchSectionUrl(String fetchSectionUrl) {
        this.fetchSectionUrl = fetchSectionUrl;
    }

    public int getDigitalCategoryID() {
        return digitalCategoryID;
    }

    public void setDigitalCategoryID(int digitalCategoryID) {
        this.digitalCategoryID = digitalCategoryID;
    }

    public int getDigitalProductID() {
        return digitalProductID;
    }

    public void setDigitalProductID(int digitalProductID) {
        this.digitalProductID = digitalProductID;
    }

    public String getDigitalProductCode() {
        return digitalProductCode;
    }

    public void setDigitalProductCode(String digitalProductCode) {
        this.digitalProductCode = digitalProductCode;
    }


    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public boolean isSaleStarted() {
        return isSaleStarted;
    }

    public PackageViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.productId);
        dest.writeInt(this.productScheduleId);
        dest.writeInt(this.productGroupId);
        dest.writeString(this.providerScheduleId);
        dest.writeString(this.providerTicketId);
        dest.writeString(this.thumbnailApp);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.tnc);
        dest.writeInt(this.convenienceFee);
        dest.writeInt(this.mrp);
        dest.writeInt(this.commission);
        dest.writeString(this.commissionType);
        dest.writeInt(this.salesPrice);
        dest.writeInt(this.sold);
        dest.writeInt(this.booked);
        dest.writeInt(this.available);
        dest.writeInt(this.minQty);
        dest.writeInt(this.maxQty);
        dest.writeString(this.providerStatus);
        dest.writeInt(this.selectedQuantity);
        dest.writeString(this.timeRange);
        dest.writeString(this.address);
        dest.writeParcelable(this.form, flags);
        dest.writeString(this.fetchSectionUrl);
        dest.writeTypedList(this.forms);
        dest.writeInt(this.digitalCategoryID);
        dest.writeInt(this.digitalProductID);
        dest.writeString(this.digitalProductCode);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
        dest.writeByte(this.isSaleStarted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.categoryId);
    }

    protected PackageViewModel(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.productScheduleId = in.readInt();
        this.productGroupId = in.readInt();
        this.providerScheduleId = in.readString();
        this.providerTicketId = in.readString();
        this.thumbnailApp = in.readString();
        this.displayName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.tnc = in.readString();
        this.convenienceFee = in.readInt();
        this.mrp = in.readInt();
        this.commission = in.readInt();
        this.commissionType = in.readString();
        this.salesPrice = in.readInt();
        this.sold = in.readInt();
        this.booked = in.readInt();
        this.available = in.readInt();
        this.minQty = in.readInt();
        this.maxQty = in.readInt();
        this.providerStatus = in.readString();
        this.selectedQuantity = in.readInt();
        this.timeRange = in.readString();
        this.address = in.readString();
        this.form = in.readParcelable(Form.class.getClassLoader());
        this.fetchSectionUrl = in.readString();
        this.forms = in.createTypedArrayList(Form.CREATOR);
        this.digitalCategoryID = in.readInt();
        this.digitalProductID = in.readInt();
        this.digitalProductCode = in.readString();
        this.startDate = in.readLong();
        this.endDate = in.readLong();
        this.isSaleStarted = in.readByte() != 0;
        this.categoryId = in.readInt();
    }

    public static final Creator<PackageViewModel> CREATOR = new Creator<PackageViewModel>() {
        @Override
        public PackageViewModel createFromParcel(Parcel source) {
            return new PackageViewModel(source);
        }

        @Override
        public PackageViewModel[] newArray(int size) {
            return new PackageViewModel[size];
        }
    };
}
