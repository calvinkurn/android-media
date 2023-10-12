<!--left header table-->
| **Applink** | <br/><br/>```<br/>val SORT_FILTER = "$INTERNAL_TOKOPEDIA_NOW/sort-filter"<br/>```<br/><br/> |  |
| --- | --- | --- |
| **FE** | [Said Faisal](https://tokopedia.atlassian.net/wiki/people/5e25eee0ee264b0e745862c3?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence)  |  |
| **Figma** | [(Figma) UX/UI - TokoNOW!](https://www.figma.com/file/ywlnYgKxnz7AYYtPDhTs9T/UX%2FUI---TokoNOW!-%5BPilot-Release---July%5D?node-id=3441%3A13051)  |  |

### **Module**

`tokopedianow`



---

### **Classes**

- `TokoNowDateFilterFragment`
- `TokoNowDateFilterBottomSheet`



---

### **Description**

![image](../../res/tokopedia_now_date_filter_bottom_sheet.png)

This bottom sheet is used for showing all available date filter options in TokoNOW, latest there are 4 options :

- **Semua Tanggal**, this is the default state of sorting
- **30 Hari Terakhir**, this is for sorting based on the latest 30 days
- **90 Hari Terakhir**, this is for sorting based on the latest 90 days
- **Pilih Tanggal Sendiri**, choose the date you want



---

### **How to Use**

1. Call RouteManager.route() with `ApplinkConstInternalTokopediaNow.DATE_FILTER`.
2. Make sure to pass an extra parcelable object value as `SelectedDateFilter`

