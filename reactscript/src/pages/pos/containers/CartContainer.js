import React, { Component } from 'react'
import { connect } from 'react-redux'
import CartItemList from '../components/pages/cart/CartItemList'
import {
  removeFromCart,
  incrementQty,
  decrementQty,
  clearCart,
  reloadState,
  fetchCartFromCache
} from '../actions/index'

const mapStateToProps = (state, ownProps) => {
  return {
    items: state.cart.items,
    visible: ownProps.visible,
    onBackPress: ownProps.onBackPress,
    totalPrice: state.cart.totalPrice,
    isFetching: state.cart.isFetching
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    clearState: () => {
      dispatch(reloadState())
    },
    fetchCartList: () => {
      dispatch(fetchCartFromCache())
    },
    onItemClick: (id) => {
    },
    onRemoveFromCart: (id) => {
      dispatch(removeFromCart(id))
    },
    onRemoveAllFromCart: () => {
      dispatch(clearCart())
    },
    onIncrQty: (id, pid, qty) => {
      dispatch(incrementQty(id, pid, qty))
    },
    onDecrQty: (id, pid, qty) => {
      dispatch(decrementQty(id, pid, qty))
    },

  }
}

const CartContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(CartItemList)

export default CartContainer
