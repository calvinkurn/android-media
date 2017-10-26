import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
import CartContainer from './containers/CartContainer'
// import InvoiceErrorPage from './components/ErrorPage'


class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                {/* <InvoiceErrorPage /> */}
                <CartContainer onBackPress={() => {}}/>
            </Provider>
        )
    }
}
export default Root