import { SessionModule } from 'NativeModules'


export const FETCH_SHOP_NAME = 'FETCH_SHOP_NAME'
export const fetchShopName = () => ({
  type: FETCH_SHOP_NAME,
  payload: SessionModule.getShopName()
    .then(res => {
      return res
    })
    .catch(err => console.log(err))
})

export const FETCH_SHOP_ID = 'FETCH_SHOP_ID'
export const fetchShopId = () => ({
  type: FETCH_SHOP_ID,
  payload: SessionModule.getShopId()
})