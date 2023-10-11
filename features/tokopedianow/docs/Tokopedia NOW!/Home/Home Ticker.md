
![image](../../res/home_ticker.png)

<!--left header table-->
| **Type Factory** | `HomeTypeFactory` |
| --- | --- |
| **View Holder** | `HomeTickerViewHolder` |
| **UI Model** | `HomeTickerUiModel` |
| **Listener** | `HomeTickerListener` |
| **Use Case** | `GetTickerUseCase` `GetHomeLayoutDataUseCase` |
| **GQL** | [HPB/Home - [API:MOJ] Ticker](/wiki/spaces/HP/pages/381419568) [HPB/Home - [API:MOJ] Channel](/wiki/spaces/HP/pages/381550603)  |
| **FE** |  [Said Faisal](https://tokopedia.atlassian.net/wiki/people/5e25eee0ee264b0e745862c3?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) |
| **BE** | [Christian Ade Yanuar](https://tokopedia.atlassian.net/wiki/people/5c370a28ff324728a1da77c4?ref=confluence) [Andreas Wijaya](https://tokopedia.atlassian.net/wiki/people/5c37093fad984b52108580ac?ref=confluence) [Jonathan Ryadi](https://tokopedia.atlassian.net/wiki/people/5c370a241c6a692feab9a87e?ref=confluence)  |

## **Note**

- It’s only used on Tokopedia NOW! Home page
- Need `GetHomeLayoutDataUseCase` to decide whether the widget need to appear or not
- If need to render the widget, use `GetTickerUseCase` to get all data

