---
title: "Logistic Seller: Return To Shipper"
labels:
- return-to-shipper
- logistic
- logistic-seller
---

<!--left header table-->
| **Status**              | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                           |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Project Contributor** | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Product Manager         | [Aulia Rasyid](https://tokopedia.atlassian.net/wiki/people/613e9e61e7c328007069f2b6?ref=confluence)                                                                                                         |
| Team                    | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                              |
| Release date            | 24 Feb 2023 / <!--start status:GREY-->MA-3.209<!--end status--> <!--start status:GREY-->SA-2.139<!--end status-->                                                                                           |
| Module type             | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                          |
| Product PRD             | [PRD: Returned to Shipper Flow Automation](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2006223361/PRD%3A+Returned+to+Shipper+Flow+Automation)                                                      |
| Module Location         | `features/logistic/logisticseller`                                                                                                                                                                          |
| Figma                   | [Figma](https://www.figma.com/file/1m6rwoPxdL5C3iSn2vDlZi/BOM-%26-SOM---Returned-to-Shipper-Flow-Automation?node-id=2%3A2&t=tgUYlDoXmLiT1nkQ-0)                                                             |

<!--toc-->

## Release Notes

<!--start expand:24 Feb 2023 (MA-3.209/SA-2.139)-->

<!--end expand-->

## Overview

### Background

Every physical goods order in Tokopedia will have a shipment. Some shipments may have issues in their deliveries, and after several number of attempts its still failing, shipper will return the item to sender. In the existing flow, if a shipment is returned to sender, the order will be stuck since theres no end state for a Returned to Shipper (RTS) case. For the existing business process you can see the details in [prd](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2006223361/PRD+Returned+to+Shipper+Flow+Automation).

### Project Description

Create a new confirmation dialog for the user to return goods. This dialog will display an image of goods to be returned. Users can process returns to shipper and can view the help page.

## Tech Stack

- `Image loader`: this feature need to implement `logisticCommon` because need to use `LogisticImageDeliveryHelper` to modify url image with several parameters dan add bearer on header glide url.

## Navigation

Entry point to show dialog from SOM Detail and SOM List 

![](../res/returntoshipper/Screen%20Shot%202022-12-09%20at%2009.47.04.png)

- On first condition will load [Get General Information](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2096759219/Get+General+Information) to get data for showing confirmation dialog


	- If success `response.status == 200`,   
	Show success dialog → `viewModel.confirmationRtsState` is `ShowRtsConfirmDialog`
	
	![](../res/returntoshipper/Screen%20Shot%202023-03-07%20at%2014.14.00.png)
	
	
		- On dismissed dialog will finish activity with result `Activity.RESULT_CANCELED`
	- If failed `response.status != 200`,   
	Show toaster (`viewModel.confirmationRtsState` is `ShowToaster`) and finish activity with result `Activity.RESULT_FIRST_USER`
	
	![](../res/returntoshipper/Screen%20Shot%202023-03-07%20at%2014.46.51.png)
- When user clicked ***Bantuan*** (secondary button) will open webview   
(url get from `data.article_url` on from response [Get General Information](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2096759219/Get+General+Information))
- When user clicked ***Ya, Sudah Terima*** (primary button) request [Request General Information](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2096828655/Request+General+Information)


	- If success `response.status == 200`,   
	Show success dialog → `viewModel.confirmationRtsState` is `ShowRtsSuccessDialog`
	
	![](../res/returntoshipper/Screen%20Shot%202022-12-09%20at%2016.33.17.png)
	
	
		- When dialog dismissed will finish activity with result `Activity.RESULT_OK`
	- If failed `response.status != 200`,   
	Show success dialog → `viewModel.confirmationRtsState` is `ShowRtsFailedDialog`
	
	![](../res/returntoshipper/Screen%20Shot%202022-12-09%20at%2016.34.23.png)
	
	
		- When dialog dismissed will finish activity with result `Activity.RESULT_FIRST_USER`

## How-to

To show return to shipper dialog you can call `ApplinkConstInternalLogistic.RETURN_TO_SHIPPER` with parameter `order_id`



```
startActivityForResult(RouteManager.getIntent(this, ApplinkConstInternalLogistic.RETURN_TO_SHIPPER,  {order_id}))
```

There are several result code that will be returned when activity finished



| **Result Code** | **Description** |
| --- | --- |
| `Activity.RESULT_OK` | Returned when users success returned to shipper |
| `Activity.RESULT_FIRST_USER` | Returned when users failed returned to shipper |
| `Activity.RESULT_CANCELED` | Returned when users do nothing / dissmiss the confirmation dialog |

### GQL List



| **GQL Name** | **Documentation** | **Description** |
| --- | --- | --- |
| `getGeneralInformation` | [Get General Information](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2096759219/Get+General+Information)  | Used for sellers get general Information about goods to be returned |
| `mpLogisticInsertActionGeneralInformation` | [Request General Information](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2096828655/Request+General+Information)  | - Used for sellers to confirm order that have been returned. (Button “Ya, sudah terima).<br/>- Used when the seller click help button. (Button “Bantuan”).<br/> |



---

## Action Items

- Implement tracker

## Useful Links

- [Figma](https://www.figma.com/file/1m6rwoPxdL5C3iSn2vDlZi/BOM-%26-SOM---Returned-to-Shipper-Flow-Automation?node-id=2%3A2&t=tgUYlDoXmLiT1nkQ-0)
- [Tech Plan](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2097028105/Return+To+Shipper)