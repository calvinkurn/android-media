import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  ADD_TO_CART,
  REMOVE_FROM_CART,
  INCREMENT_QTY,
  DECREMENT_QTY,
  CLEAR_CART,
  FETCH_CART_FROM_CACHE,
} from '../actions/index'



export const cart = (state = {
    items: [],
    totalPrice: 0,
    isFetching: false,
  }, action) => {
    switch (action.type) {
      case `${FETCH_CART_FROM_CACHE}_${PENDING}`:
        return {
          ...state,
          isFetching: true,
        }
      case `${FETCH_CART_FROM_CACHE}_${FULFILLED}`:
        const cartItems = action.payload.list
        const getTotalPrice = () => {
          let total_price = 0
  
          action.payload.list.map(res => {
            let price_per_item = res.product.product_price_unformatted * res.quantity
            total_price = total_price + price_per_item
          })
          return total_price
        }
  
        return {
          items: cartItems || [],
          totalPrice: getTotalPrice(),
          isFetching: false,
        }
  
      // case ADD_TO_CART:
      //   return {
      //     items: [...state.items, action.payload.item],
      //     totalPrice: state.totalPrice + action.payload.item.price
      //   }
      case `${FETCH_CART_FROM_CACHE}_${REJECTED}`:
        return {
          ...state,
          isFetching: false,
        }
      case ADD_TO_CART:
  
        return {
          items: [...state.items, action.payload.item],
          totalPrice: state.totalPrice + action.payload.item.price
        }
  
      case `${REMOVE_FROM_CART}_${FULFILLED}`:
        console.log(action.payload)
        const ItemToBeRemoved = state.items.filter(i => i.product_id === action.payload.pid)
  
        return {
          items: state.items.filter(i => action.payload.pid !== i.product_id),
          totalPrice: state.totalPrice - (ItemToBeRemoved[0].product.product_price_unformatted * ItemToBeRemoved[0].quantity)
        }
  
      case `${INCREMENT_QTY}_${FULFILLED}`:
        const itemQtyToBeIncr = state.items.filter(i => i.product_id === action.payload.pid)
        return {
          ...state,
          items: state.items.map(b => {
            if (action.payload.pid === b.product_id) {
              return Object.assign({}, b, {
                quantity: action.payload.quantity
              })
            } else {
              return b
            }
          }),
          totalPrice: state.totalPrice + itemQtyToBeIncr[0].product.product_price_unformatted
        }
  
      case `${DECREMENT_QTY}_${FULFILLED}`:
        const itemQtyToBeDecr = state.items.filter(i => i.product_id === action.payload.pid)
        if (itemQtyToBeDecr[0].quantity === 1) {
          return state
        }
  
        return {
          items: state.items.map(b => {
            if (action.payload.pid === b.product_id) {
              return Object.assign({}, b, {
                quantity: action.payload.quantity
              })
            } else {
              return b
            }
          }),
          totalPrice: state.totalPrice - itemQtyToBeDecr[0].product.product_price_unformatted
        }
  
      case `${CLEAR_CART}_${FULFILLED}`:
        return {
          items: [],
          totalPrice: 0
        }
      default:
        return state
    }
  }